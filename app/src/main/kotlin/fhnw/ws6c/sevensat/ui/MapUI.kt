package fhnw.ws6c.sevensat.ui

import android.app.Activity
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
import com.mapbox.maps.ResourceOptionsManager
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapUI(model: SevenSatModel, mapModel: MapModel, scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState) {
  mapModel.addUserPositionToMap()
  model.loadSatellites(LocalContext.current)
  model.refreshSatellites()
  Row {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .background(Color.Black)
        .fillMaxSize()
    ) {
      MapView(model, mapModel, scope, scaffoldState)
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MapView(
  model: SevenSatModel,
  mapModel: MapModel,
  scope: CoroutineScope,
  scaffoldState: BottomSheetScaffoldState
) {
  AndroidView(
    modifier = Modifier,
    update = {
      mapModel.clearSatellites()
      model.satellitesMap.forEach { satellite ->
        mapModel.addSatellite(satellite.key, satellite.value)
      }
      //TODO: very slow for many points
      mapModel.addFlightLine(model.clickedSatelliteRoute)
    },
    factory = { context ->
      ResourceOptionsManager.getDefault(context, context.getString(R.string.mapbox_access_token))
      val map = mapModel.getMapView()
      mapModel.onSatellitePointClick { norad ->
        onSatelliteClick(
          model,
          norad,
          scope,
          scaffoldState
        )
      }
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
}

@OptIn(ExperimentalMaterialApi::class)
fun onSatelliteClick(model: SevenSatModel, clickedSatelliteNorad: Long, scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState) {
  val found = model.satellitesMap.filter { it.key.noradId == clickedSatelliteNorad }
  if (found.isNotEmpty()) {
    scope.launch { model.calculateFlightLineForSatellite(clickedSatelliteNorad) }
    val sat = found.entries.iterator().next().key
    model.selectedSatellites.add(sat)
    scope.launch {scaffoldState.bottomSheetState.expand()}
  }
  println("Clicked Norad: $clickedSatelliteNorad")
}
