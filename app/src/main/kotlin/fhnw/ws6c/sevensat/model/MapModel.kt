package fhnw.ws6c.sevensat.model

import android.app.Activity
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.JsonObject
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.gps.UserLocationSubject
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.util.extensions.toBitMap
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import fhnw.ws6c.sevensat.util.linalg.Linalg
import java.util.*

class MapModel(private val context: Activity) {
  private val userPositionCallbackKey     = "UserPosition"
  private val mapView: MapView            = MapView(context)
  private var satelliteAnnotationManager  = mapView.annotations.createPointAnnotationManager()
  private var pointAnnotationManager      = mapView.annotations.createPointAnnotationManager()
  // this state must be kept, to remove or replace the listener later.
  private var satellitePointClickListener = OnPointAnnotationClickListener { false }
  private var userLocationSubject         = UserLocationSubject(context){}
  private val satellitePoints             = mutableListOf<PointAnnotation>()
  private var flightLine                  = emptyList<PointAnnotation>()
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

  fun addSatellite(
    sat: Satellite,
    satPosition: SatPos,
  ) {
    // Create an instance of the Annotation API and get the PointAnnotationManager.
    AppCompatResources.getDrawable(context, R.drawable.sat_horizontal)?.toBitMap()?.let {
      this.clearSatellites()
      val data = JsonObject()
      data.addProperty("id", sat.noradId)
      // Set options for the resulting symbol layer.
      val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        // Define a geographic coordinate.
        .withPoint(Point.fromLngLat(satPosition.longDeg(), satPosition.latDeg()))
        .withIconImage(it)
        .withIconColor("White")
        .withIconSize(.5)
        .withIconRotate(getSatelliteRotation(sat, satPosition))
        .withData(data)

      // Add the resulting pointAnnotation to the map.
      val newSatellitePoint = satelliteAnnotationManager.create(pointAnnotationOptions)
      satellitePoints.add(newSatellitePoint)
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
    AppCompatResources.getDrawable(context, R.drawable.point)?.toBitMap()?.let {
      val pointAnnotations = points.map { point ->
        // Set options for the resulting symbol layer.
        val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
          .withPoint(Point.fromLngLat(point.longDeg(), point.latDeg()))
          .withIconImage(it)
          .withIconColor("White")
          .withIconSize(.1)
        pointAnnotationManager.create(pointAnnotationOptions)
      }
      flightLine = pointAnnotations
    }
  }

  private fun clearSatellites() {
    satelliteAnnotationManager.delete(satellitePoints)
    satellitePoints.clear()
  }

  private fun deleteCurrentMapLine() {
    pointAnnotationManager.delete(flightLine)
    flightLine = emptyList()
  }

  private fun deleteCurrentUserAnnotation() =
    currentUserAnnotation?.let {
      pointAnnotationManager.delete(it)
    }
}
