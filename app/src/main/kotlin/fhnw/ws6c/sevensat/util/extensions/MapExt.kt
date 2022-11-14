package fhnw.ws6c.sevensat.util.extensions

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPolylineAnnotationManager
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos

fun MapView.addSatellite(satPosition: SatPos, context: Context) {
  // Create an instance of the Annotation API and get the PointAnnotationManager.
  AppCompatResources.getDrawable(context, R.drawable.sat)?.toBitMap()?.let {

    val pointAnnotationManager = this.annotations.createPointAnnotationManager()
    pointAnnotationManager.deleteAll()
    // Set options for the resulting symbol layer.
    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
    // Define a geographic coordinate.
      .withPoint(Point.fromLngLat(satPosition.longDeg(), satPosition.latDeg()))
    // Specify the bitmap you assigned to the point annotation
    // The bitmap will be added to map style automatically.

      .withIconImage(it)
      .withIconSize(.5)
      .withIconRotate(90.0)
    // Add the resulting pointAnnotation to the map.
    pointAnnotationManager.create(pointAnnotationOptions)
  }

}

fun MapView.addFlightLine(points: List<SatPos>, context:Context) {
  val lineAnnotationManager = this.annotations.createPolylineAnnotationManager()
  val options = PolylineAnnotationOptions()

  options.withPoints(points.map { Point.fromLngLat(it.longDeg(), it.latDeg()) })
  lineAnnotationManager.deleteAll()
//  // Set options for the resulting symbol layer.
//  val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
//    // Define a geographic coordinate.
//    .withPoint(Point.fromLngLat(satPosition.longDeg(), satPosition.latDeg()))
//    // Specify the bitmap you assigned to the point annotation
//    // The bitmap will be added to map style automatically.
//
//    .withIconImage(it)
//    .withIconSize(.5)
//    .withIconRotate(90.0)
  // Add the resulting pointAnnotation to the map.
  lineAnnotationManager.create(options)
}
