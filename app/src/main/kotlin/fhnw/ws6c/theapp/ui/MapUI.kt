package fhnw.ws6c.theapp.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.scale
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import fhnw.ws6c.R


@Composable
fun MapUI(activity: ComponentActivity) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .background(Color.Black)
      .fillMaxSize()
  ) {
    val localContext = LocalContext.current
    AndroidView(
      modifier = Modifier,
      factory = { context ->
        ResourceOptionsManager.getDefault(
          context,
          context.getString(R.string.mapbox_access_token)
        )
       val map = MapView(context)
        map.apply {
          getMapboxMap().loadStyleUri(
            Style.DARK
          ) {
            addAnnotationToMap(map, localContext)
            cameraOptions {
              zoom(19.0)
            }
          }
        }
      })
  }

}


private fun addAnnotationToMap(mapView :MapView, activity: Context) {
// Create an instance of the Annotation API and get the PointAnnotationManager.

    bitmapFromDrawableRes(activity, R.drawable.sat)?.let {


      val annotationApi = mapView.annotations
      val pointAnnotationManager = annotationApi.createPointAnnotationManager()
// Set options for the resulting symbol layer.
      val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
        .withPoint(Point.fromLngLat(8.220250, 47.478519 ))
// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.

        .withIconImage(it)
        .withIconSize(.5)
        .withIconRotate(90.0)
// Add the resulting pointAnnotation to the map.
      pointAnnotationManager.create(pointAnnotationOptions)
    }
}
private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
  convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
  if (sourceDrawable == null) {
    return null
  }
  return if (sourceDrawable is BitmapDrawable) {

    sourceDrawable.bitmap
  } else {
// copying drawable object to not manipulate on the same reference
    val constantState = sourceDrawable.constantState ?: return null
    val drawable = constantState.newDrawable().mutate()
    val bitmap: Bitmap = Bitmap.createBitmap(
      drawable.intrinsicWidth, drawable.intrinsicHeight,
      Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    bitmap
  }
}
