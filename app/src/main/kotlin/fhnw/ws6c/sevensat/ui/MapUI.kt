package fhnw.ws6c.sevensat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.util.extensions.addSatellite
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapUI(model: SevenSatModel, scaffoldState: BottomSheetScaffoldState, scope: CoroutineScope) {
  model.refreshSatellites()
  Row {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
    ) {
      val localContext = LocalContext.current
      AndroidView(
        modifier = Modifier,
        update = { mapView ->
          model.satellitesMap.values.forEach { satPos ->
            mapView.addSatellite(satPos, localContext){ satellite -> model.selectedSatellite = satellite; scope.launch {scaffoldState.bottomSheetState.expand()}
            }
            println("lat: ${satPos.latDeg()}, long: ${satPos.longDeg() - 360}")
          }
          model.clickedSatelliteRoute.forEach {
            mapView.addSatellite(it, localContext){}
          }
        },
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
              cameraOptions {
                zoom(19.0)
              }
            }
          }
        })
      Button(onClick = { model.loadSatellites() }) {
        Text(text = "Load satellites")
      }
    }
  }

}
