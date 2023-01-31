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
import com.mapbox.maps.extension.style.layers.generated.circleLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconRotationAlignment
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
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

private const val SWAP_ZOOM_LEVEL = 4.2

class MapModel(private val context: Activity) {
  private val userPositionCallbackKey     = "UserPosition"
  private val mapView: MapView            = MapView(context)
  private var pointAnnotationManager      = mapView.annotations.createPointAnnotationManager()
  // this state must be kept, to remove or replace the listener later.
  private var userLocationSubject         = UserLocationSubject(context){}
  private var currentUserAnnotation       : PointAnnotation?  = null

  init {
    initLineLayer()
    initSatelliteLayers()
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
          onClick(norad)
        }}
      true
    }
  }

  fun refreshSatellitePositionOnMap(satellites: Map<Satellite, SatPos>) {
    mapView.getMapboxMap().executeOnRenderThread {
      val noradKey          = "norad"
      val rotationAngleKey  = "rotationAngle"

      val features = FeatureCollection.fromFeatures(satellites.entries.map { entry ->
        val norad     = entry.key.noradId
        val long      = entry.value.longDeg()
        val lat       = entry.value.latDeg()
        val altitude  = 10000.0
        val data      = JsonObject()
        var rotation  = getSatelliteRotation(entry.key, entry.value)
        rotation      = if(rotation.isNaN()) 0.0 else rotation
        data.addProperty(noradKey, norad)
        data.addProperty(rotationAngleKey, rotation)
        Feature.fromGeometry(Point.fromLngLat(long, lat, altitude), data)
      })

      mapView.getMapboxMap().getStyle { style ->
        val source = style.getSourceAs<GeoJsonSource>(context.getString(R.string.SATELLITES_SOURCE))
        source?.featureCollection(features)
      }
    }
  }

  fun addFlightLine(points: List<SatPos>) {
    val pointLists  = getFlightLinesFromPoints(points).filter { it.size > 1 }
    val feature     = pointLists.map { Feature.fromGeometry(LineString.fromLngLats(it)) }
    val features    = FeatureCollection.fromFeatures(feature)

    mapView.getMapboxMap().executeOnRenderThread {
      mapView.getMapboxMap().getStyle { style ->
        val source = style.getSourceAs<GeoJsonSource>(context.getString(R.string.LINE_SOURCE))
        source?.featureCollection(features)
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
          .center(Point.fromLngLat(location.longitude().toDegrees(), location.latitude().toDegrees()))
          // specifies the zoom value. Increase or decrease to zoom in or zoom out
          .zoom(8.0)
          // specify frame of reference from the center.
          .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
          .build(),
        mapAnimationOptions
      )
    }
  }

  private fun initLineLayer () {
    mapView.getMapboxMap().getStyle { style ->
      style.addSource(geoJsonSource(context.getString(R.string.LINE_SOURCE)){
        this.build()
      })
      style.addLayer(
        lineLayer(
          context.getString(R.string.LINE_LAYER),
          context.getString(R.string.LINE_SOURCE)
        ) {
          this.lineColor(Color.LTGRAY)
          this.lineWidth(2.0)
          this.lineJoin(LineJoin.ROUND)
        }
      )
    }
  }

  private fun initSatelliteLayers() {
    println("MapModel.initSatelliteLayers")
    val satImageId        = "sat_horizontal"
    val rotationAngleKey  = "rotationAngle"

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
          circleLayer(
            context.getString(R.string.SATELLITES_LAYER_CIRCLE),
            context.getString(R.string.SATELLITES_SOURCE)
          ) {
            this.circleColor(Color.WHITE)
            this.circleRadius(1.5)
            this.maxZoom(SWAP_ZOOM_LEVEL)
          }
        )

        style.addLayer(
          symbolLayer(
            context.getString(R.string.SATELLITES_LAYER),
            context.getString(R.string.SATELLITES_SOURCE)
          ) {
            this.iconImage(satImageId)
            this.iconSize(0.3)
            this.minZoom(SWAP_ZOOM_LEVEL)
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
  private fun getFlightLinesFromPoints(points: List<SatPos>): List<List<Point>> {
    val ps = points.map { Point.fromLngLat(it.longDeg(), it.latDeg()) }
    var lastPoint = ps.first()
    val separatorPoint = ps.find {
      val result = (lastPoint.longitude() - it.longitude()).absoluteValue > 180
      lastPoint = it
      result
    } ?: ps.last()

    val separator = ps.indexOf(separatorPoint)
    val firstPart = ps.subList(0, separator).toMutableList()

    var secondPart: List<Point> = emptyList()

    if (separator != ps.lastIndex) {
      val longBefore      = ps[separator - 1].longitude()
      val latBefore       = ps[separator - 1].latitude()

      val longAfter       = separatorPoint.longitude()
      val latAfter        = separatorPoint.latitude()

      val dLong           = 360 + longAfter - longBefore
      val dNew            = 360 - longBefore

      val dLat            = latAfter - latBefore
      val ratio           = dLat / dLong

      val boundaryLat     = latBefore + ratio * dNew
      val sepLong         = separatorPoint.longitude()
      var firstPartEnd    = 0.0
      var secondPartStart = 360.0
      if ((360 - sepLong).absoluteValue > (0 - sepLong).absoluteValue) {
        // nearer to zero than 360
        firstPartEnd      = secondPartStart
        secondPartStart   = 0.0
      }
      firstPart           += Point.fromLngLat(firstPartEnd, boundaryLat)
      secondPart          = mutableListOf(Point.fromLngLat(secondPartStart, boundaryLat))
      secondPart          = secondPart + ps.subList(separator, ps.lastIndex)
    }

    return listOf(firstPart, secondPart)
  }

  private fun deleteCurrentUserAnnotation() =
    currentUserAnnotation?.let {
      pointAnnotationManager.delete(it)
    }
}
