package fhnw.ws6c.theapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import fhnw.ws6c.R


@Composable
fun MapUI() {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .background(Color.Black)
      .fillMaxSize()
  ) {
    AndroidView(
      modifier = Modifier,
      factory = { context ->
        ResourceOptionsManager.getDefault(
          context,
          context.getString(R.string.mapbox_access_token)
        )
        MapView(context).apply {
          getMapboxMap().loadStyleUri(Style.DARK) {
            cameraOptions {
              zoom(19.0)
            }
          }
        }
      })
  }
}
