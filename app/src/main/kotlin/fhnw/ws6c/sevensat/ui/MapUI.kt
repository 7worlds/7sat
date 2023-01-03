package fhnw.ws6c.sevensat.ui

import android.content.Context
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
import com.mapbox.maps.extension.style.layers.properties.generated.ProjectionName
import com.mapbox.maps.extension.style.projection.generated.Projection
import com.mapbox.maps.extension.style.projection.generated.setProjection
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

private val backgroundJob = SupervisorJob()
private val modelScope = CoroutineScope(backgroundJob + Dispatchers.IO)


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapUI(
  model: SevenSatModel,
  mapModel: MapModel,
  scope: CoroutineScope,
  scaffoldState: BottomSheetScaffoldState
) {
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
    },
    factory = { context ->
      ResourceOptionsManager.getDefault(context, context.getString(R.string.mapbox_access_token))
      val map = mapModel.getMapView()

      mapModel.addSatelliteClickListener { norad ->
        onSatelliteClick(
          model,
          mapModel,
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
          it.setProjection(Projection(ProjectionName.GLOBE))
        }
      }
    })
}

@OptIn(ExperimentalMaterialApi::class)
fun onSatelliteClick(
  model: SevenSatModel,
  mapModel: MapModel,
  clickedSatelliteNorad: Long,
  scope: CoroutineScope,
  scaffoldState: BottomSheetScaffoldState
) {

  val found = model.satellitesMap.filter { it.key.noradId == clickedSatelliteNorad }
  if (found.isNotEmpty()) {

    model.calculateFlightLineForSatellite(clickedSatelliteNorad) { route ->
      mapModel.addFlightLine(route)
    }
    modelScope.launch {
      model.getSatelliteDetails(found.keys.first())
    }
    scope.launch {
      if (scaffoldState.bottomSheetState.isExpanded) {
        scaffoldState.bottomSheetState.collapse()
      }
      scaffoldState.bottomSheetState.expand()
    }
  }
}
