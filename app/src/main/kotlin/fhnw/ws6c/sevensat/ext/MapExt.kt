package fhnw.ws6c.sevensat.ext

import android.content.Context
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.Satellite
//import fhnw.ws6c.sevensat.ui.bitmapFromDrawableRes

fun MapView.addSatellite(sat: Satellite) {

}


//private fun addAnnotationToMap(mapView: MapView, activity: Context) {
//// Create an instance of the Annotation API and get the PointAnnotationManager.
//
//  bitmapFromDrawableRes(activity, R.drawable.sat)?.let {
//
//
//    val annotationApi = mapView.annotations
//    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
//// Set options for the resulting symbol layer.
//    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
//// Define a geographic coordinate.
//      .withPoint(Point.fromLngLat(8.220250, 47.478519))
//// Specify the bitmap you assigned to the point annotation
//// The bitmap will be added to map style automatically.
//
//      .withIconImage(it)
//      .withIconSize(.5)
//      .withIconRotate(90.0)
//// Add the resulting pointAnnotation to the map.
//    pointAnnotationManager.create(pointAnnotationOptions)
//  }
//}
