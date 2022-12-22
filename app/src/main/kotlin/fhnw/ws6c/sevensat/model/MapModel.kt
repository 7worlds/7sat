package fhnw.ws6c.sevensat.model

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.JsonObject
import com.mapbox.geojson.*
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconRotationAlignment
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSourceAs
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.gps.UserLocationSubject
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.util.extensions.toBitMap
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import fhnw.ws6c.sevensat.util.linalg.Linalg
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.tan

class MapModel(private val context: Activity) {
  private val userPositionCallbackKey = "UserPosition"
  private val mapView: MapView = MapView(context)
  private var satelliteAnnotationManager = mapView.annotations.createPointAnnotationManager()
  private var polyLineAnnotationManager = mapView.annotations.createPolylineAnnotationManager()
  private var pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

  private var userLocationSubject = UserLocationSubject(context) {}
  private val satellitePoints = mutableListOf<PointAnnotation>()
  private var currentUserAnnotation: PointAnnotation? = null

  init {
    initSatelliteLayer()
    flyToUserPosition()
  }

  fun getMapView() = mapView

  fun addUserPositionToMap() {
    userLocationSubject.addLocationObserver(userPositionCallbackKey) { location ->
      deleteCurrentUserAnnotation()
      AppCompatResources.getDrawable(context, R.drawable.userposition)?.toBitMap()?.let {
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
          .withPoint(Point.fromLngLat(location.longitude, location.latitude))
          .withIconImage(it)
          .withIconColor("White")
          .withIconSize(.5)
        currentUserAnnotation = pointAnnotationManager.create(pointAnnotationOptions)
      }
    }
  }

  fun addSatelliteClickListener(onClick: (norad: Long) -> Unit){
    mapView.getMapboxMap().addOnMapClickListener {
      val pixel = mapView.getMapboxMap().pixelForCoordinate(it)
      mapView.getMapboxMap().queryRenderedFeatures(RenderedQueryGeometry(pixel),
        RenderedQueryOptions(
          listOf(context.getString(R.string.SATELLITES_LAYER)),
          null
        )
      ) { found ->
        if (found.isValue && found.value!!.isNotEmpty()) {
          val norad = found.value!![0].feature.getStringProperty("norad").toLong()
          println(norad)
          onClick(norad)
        }}
      true
    }
  }

  fun refreshSatellitePositionOnMap(satellites: Map<Satellite, SatPos>) {
    val noradKey = "norad"
    val rotationAngleKey = "rotationAngle"

    val features = FeatureCollection.fromFeatures(satellites.map { entry ->
      val norad = entry.key.noradId
      val long = entry.value.longDeg()
      val lat = entry.value.latDeg()
      val data = JsonObject()
      data.addProperty(noradKey, norad)
      data.addProperty(rotationAngleKey, getSatelliteRotation(entry.key, entry.value))
      Feature.fromGeometry(Point.fromLngLat(long,lat), data)
    })

    mapView.getMapboxMap().executeOnRenderThread{
      mapView.getMapboxMap().getStyle { style ->
        val source = style.getSourceAs<GeoJsonSource>(context.getString(R.string.SATELLITES_SOURCE))
        source?.featureCollection(features)
      }
    }
  }

  fun addFlightLine(points: List<SatPos>) {
    deleteCurrentMapLine()
    if (points.isNotEmpty()) {
      val multiLineString = getFlightLinesFromPoints(points)
      multiLineString.lineStrings().forEach {
        val polyLineAnnotationOptions = PolylineAnnotationOptions()
          .withLineWidth(2.0)
          .withLineColor(Color.WHITE)
          .withGeometry(it)
        polyLineAnnotationManager.create(polyLineAnnotationOptions)
      }
    }
  }

  private fun flyToUserPosition() {
    currentUserAnnotation?.let {
      val location = it.point
      val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
      mapView.camera.easeTo(
        CameraOptions.Builder()
          // Centers the camera to the lng/lat specified.
          .center(
            Point.fromLngLat(
              location.longitude().toDegrees(),
              location.latitude().toDegrees()
            )
          )
          // specifies the zoom value. Increase or decrease to zoom in or zoom out
          .zoom(8.0)
          // specify frame of reference from the center.
          .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
          .build(),
        mapAnimationOptions
      )
    }
  }

  private fun initSatelliteLayer () {
    val satImageId = "sat_horizontal"
    val rotationAngleKey = "rotationAngle"

      mapView.getMapboxMap().getStyle { style ->
    AppCompatResources.getDrawable(context, R.drawable.sat_horizontal)?.toBitMap()?.let { icon ->

        style.addImage(
          satImageId,
          icon
        )
        style.addSource(geoJsonSource(context.getString(R.string.SATELLITES_SOURCE)) {
          this.build()
        })

        style.addLayer(
          symbolLayer(
            context.getString(R.string.SATELLITES_LAYER),
            context.getString(R.string.SATELLITES_SOURCE)
          ) {
            this.iconImage(satImageId)
            this.iconSize(0.4)
            this.iconRotate(get { literal(rotationAngleKey) })
            this.iconRotationAlignment(IconRotationAlignment.MAP)
          })
      }
    }
  }

  private fun getSatelliteRotation(sat: Satellite, currentPosition: SatPos): Double {
    val futurePoint = sat.getPosition(Date().time + 1000)
    return Linalg.angleBetweenPoints(
      Point.fromLngLat(currentPosition.longitude, currentPosition.latitude),
      Point.fromLngLat(futurePoint.longitude, futurePoint.latitude)
    )
  }

  @SuppressLint("Range")
  private fun getFlightLinesFromPoints(points: List<SatPos>): MultiLineString {
    val ps = points.map { Point.fromLngLat(it.longDeg(), it.latDeg()) }
    var lastPoint = ps.first()
    val lineParts = mutableListOf<LineString>()
    val separatorPoint = ps.find {
      val result = (lastPoint.longitude() - it.longitude()).absoluteValue > 180
      lastPoint = it
      result
    } ?: ps.last()

    val separator = ps.indexOf(separatorPoint)
    val firstPart = ps.subList(0, separator).toMutableList()

    if (separator != ps.lastIndex) {
      val longBefore = ps[separator - 1].longitude()
      val latBefore = ps[separator - 1].latitude()

      val longAfter = separatorPoint.longitude()
      val latAfter = separatorPoint.latitude()
//      println("separator long: $longAfter  lat: $latAfter")
//      println("before separator long: $longBefore  lat: $latBefore")

      var dLong = 360 + longAfter - longBefore
      var dNew = 360 - longBefore

//      if(longBefore < longAfter) {
//        dLong = 360 + longBefore - longAfter
//        dNew = 360 - longAfter
//      }

      val dLat = latAfter - latBefore
      val ratio = dLat / dLong
      val angle = tan(ratio)
//
//      println("ratio $ratio")
//      println("dLong: $dLong")
//      println("dLat: $dLat")
//      println("angle: $angle")
//
//      println("dNew: $dNew")

      var boundaryLat = latBefore + ratio * dNew
//      println("boundaryLat: $boundaryLat")

      // add 0 & 360 longitude point to point list
      val sepLong = separatorPoint.longitude()
      var firstPartEnd = 0.0
      var secondPartStart = 360.0
      if ((360 - sepLong).absoluteValue > (0 - sepLong).absoluteValue) {
        // nearer to zero than 360
        firstPartEnd = secondPartStart
        secondPartStart = 0.0
      }
      firstPart += Point.fromLngLat(firstPartEnd, boundaryLat)
      val secondPart = mutableListOf(Point.fromLngLat(secondPartStart, boundaryLat))
      secondPart += ps.subList(separator, ps.lastIndex)
      lineParts.add(LineString.fromLngLats(secondPart))
    }
    lineParts.add(LineString.fromLngLats(firstPart))

    return MultiLineString.fromLineStrings(lineParts);
  }


  private fun deleteCurrentMapLine() {
    polyLineAnnotationManager.deleteAll()
  }

  private fun deleteCurrentUserAnnotation() =
    currentUserAnnotation?.let {
      pointAnnotationManager.delete(it)
    }
}
