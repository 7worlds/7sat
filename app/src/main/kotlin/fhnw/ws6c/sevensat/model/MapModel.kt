package fhnw.ws6c.sevensat.model

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.JsonObject
import com.mapbox.geojson.*
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.gps.UserLocationSubject
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.util.extensions.toBitMap
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import fhnw.ws6c.sevensat.util.linalg.Linalg
import java.util.*
import kotlin.math.absoluteValue


class MapModel(private val context: Activity) {
  private val userPositionCallbackKey     = "UserPosition"
  private val mapView: MapView            = MapView(context)
  private var satelliteAnnotationManager  = mapView.annotations.createPointAnnotationManager()
  private var polyLineAnnotationManager       = mapView.annotations.createPolylineAnnotationManager()
  private var pointAnnotationManager      = mapView.annotations.createPointAnnotationManager()
  // this state must be kept, to remove or replace the listener later.
  private var satellitePointClickListener = OnPointAnnotationClickListener { false }
  private var userLocationSubject         = UserLocationSubject(context){}
  private val satellitePoints             = mutableListOf<PointAnnotation>()
  fun getMapView()                        = mapView
  private var currentUserAnnotation       : PointAnnotation?  = null

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

  fun flyToUserPosition() {
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

  fun onSatellitePointClick(callback: (norad: Long) -> Unit) {
    // first remove old listener
    satelliteAnnotationManager.removeClickListener(satellitePointClickListener)
    satellitePointClickListener = OnPointAnnotationClickListener { point ->
      if (point.getData() is JsonObject) {
        val norad = (point.getData() as JsonObject).get("id")?.asLong
        if (norad != null) callback(norad)
      }
      false
    }
    // register new listener & call callback with norad
    satelliteAnnotationManager.addClickListener(satellitePointClickListener)
  }

  fun addSatellites(
    satellites: Map<Satellite, SatPos>
  ) {
    // Create an instance of the Annotation API and get the PointAnnotationManager.
    AppCompatResources.getDrawable(context, R.drawable.sat_horizontal)?.toBitMap()?.let { icon ->
      val annotations = satellites.map {
        val data = JsonObject()
        data.addProperty("id", it.key.noradId)
        // Set options for the resulting symbol layer.
        PointAnnotationOptions()
          // Define a geographic coordinate.
          .withPoint(Point.fromLngLat(it.value.longDeg(), it.value.latDeg()))
          .withIconImage(icon)
          .withIconColor("White")
          .withIconSize(.5)
          .withIconRotate(getSatelliteRotation(it.key, it.value))
          .withData(data)
      }
      // Add the resulting pointAnnotation to the map.
      val newSatellitePoint = satelliteAnnotationManager.create(annotations)
      satellitePoints.addAll(newSatellitePoint)
    }
  }


  private fun getSatelliteRotation(sat: Satellite, currentPosition: SatPos): Double {
    val futurePoint = sat.getPosition(Date().time + 1000)
    return Linalg.angleBetweenPoints(
      Point.fromLngLat(currentPosition.longitude, currentPosition.latitude),
      Point.fromLngLat(futurePoint.longitude, futurePoint.latitude)
    )
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
      // point in front of the separator point
      val beforeSepLong = ps[separator - 1].longitude()
      val beforeSepLat = ps[separator - 1].latitude()

      val dLong = 360 + separatorPoint.longitude() - beforeSepLong
      val dNew = 360  - beforeSepLong

      val dLat = separatorPoint.latitude() - beforeSepLat
      val ratio = dLat / dLong

      // calculates the latitude of the point for each side
      val boundaryLat = beforeSepLat + ratio * dNew

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

  fun clearSatellites() {
    satelliteAnnotationManager.delete(satellitePoints)
    satellitePoints.clear()
  }

  private fun deleteCurrentMapLine() {
    polyLineAnnotationManager.deleteAll()
  }

  private fun deleteCurrentUserAnnotation() =
    currentUserAnnotation?.let {
      pointAnnotationManager.delete(it)
    }
}
