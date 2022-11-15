package fhnw.ws6c.sevensat.model

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.JsonObject
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.util.extensions.toBitMap

class MapModel(private val context: Context) {
  private val mapView: MapView = MapView(context)
  private var satelliteAnnotationManager = mapView.annotations.createPointAnnotationManager()
  private var pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
  // this state must be kept, to remove or replace the listener later.
  private var satellitePointClickListener = OnPointAnnotationClickListener { false }

  private val satellitePoints = mutableListOf<PointAnnotation>()
  private var flightLine = emptyList<PointAnnotation>()

  fun getMapContext() = context

  fun getMapView() = mapView

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
    satPosition: SatPos
  ) {
    // Create an instance of the Annotation API and get the PointAnnotationManager.
    AppCompatResources.getDrawable(context, R.drawable.sat)?.toBitMap()?.let {
      this.clearSatellites()
      val data = JsonObject()
      data.addProperty("id", sat.noradId)
      // Set options for the resulting symbol layer.
      val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        // Define a geographic coordinate.
        .withPoint(com.mapbox.geojson.Point.fromLngLat(satPosition.longDeg(), satPosition.latDeg()))
        .withIconImage(it)
        .withIconColor("White")
        .withIconSize(.5)
        .withIconRotate(90.0)
        .withData(data)

      // Add the resulting pointAnnotation to the map.
      val newSatellitePoint = satelliteAnnotationManager.create(pointAnnotationOptions)
      satellitePoints.add(newSatellitePoint)
    }
  }

  fun addFlightLine(points: List<SatPos>) {

    deleteCurrentMapLine()
    val start = System.currentTimeMillis()
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
      val end = System.currentTimeMillis()
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
}
