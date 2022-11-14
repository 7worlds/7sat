package fhnw.ws6c.sevensat.util.extensions

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite

object MapModel {
  private val satellitePoints = mutableListOf<PointAnnotation>()
  private var flightLine = emptyList<PointAnnotation>()

  private var pointAnnotationManager: PointAnnotationManager? = null

  fun getPointAnnotationManager(mapView: MapView): PointAnnotationManager {
    if (pointAnnotationManager == null) {
      pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
    }
    return pointAnnotationManager!!
  }

  fun addSatellitePoint(satPoint: PointAnnotation) = satellitePoints.add(satPoint)

  fun clearSatellitePoints() = satellitePoints.clear()

  fun getAllSatellitesPoints() = satellitePoints

  fun getFlightLinePoints() = flightLine
  fun setFlightLinePoints(f: List<PointAnnotation>) {
    flightLine = f
  }
}

fun MapView.clearSatellites() {
  MapModel.getPointAnnotationManager(this).delete(MapModel.getAllSatellitesPoints())
  MapModel.clearSatellitePoints()
}

fun MapView.deleteCurrentMapLine() {
  MapModel.getPointAnnotationManager(this).delete(MapModel.getFlightLinePoints())
  MapModel.setFlightLinePoints(emptyList())
}

fun MapView.addSatellite(
  satPosition: SatPos,
  context: Context,
  onClick: (satellite: Satellite) -> Unit
) {
  // Create an instance of the Annotation API and get the PointAnnotationManager.
  AppCompatResources.getDrawable(context, R.drawable.sat)?.toBitMap()?.let {
    this.clearSatellites()
    val pointAnnotationManager = MapModel.getPointAnnotationManager(this)

    // Set options for the resulting symbol layer.
    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
      // Define a geographic coordinate.
      .withPoint(Point.fromLngLat(satPosition.longDeg(), satPosition.latDeg()))
      .withIconImage(it)
      .withIconColor("White")
      .withIconSize(.5)
      .withIconRotate(90.0)

    // Add the resulting pointAnnotation to the map.
    val newSatellitePoint = pointAnnotationManager.create(pointAnnotationOptions)
    MapModel.addSatellitePoint(newSatellitePoint)
  }
}


fun MapView.addFlightLine(points: List<SatPos>, context: Context) {
  val pointAnnotationManager = MapModel.getPointAnnotationManager(this)

  this.deleteCurrentMapLine()
  val start = System.currentTimeMillis()
  AppCompatResources.getDrawable(context, R.drawable.point)?.toBitMap()?.let {
    val pointAnnotations = points.map { point ->
      // Set options for the resulting symbol layer.
      val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(point.longDeg(), point.latDeg()))
        .withIconImage(it)
        .withIconColor("White")
        .withIconSize(.3)
        pointAnnotationManager.create(pointAnnotationOptions)
    }
    MapModel.setFlightLinePoints(pointAnnotations)
    val end = System.currentTimeMillis()
  }
}
