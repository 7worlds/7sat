package fhnw.ws6c.sevensat.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fhnw.ws6c.sevensat.SevenSatApp
import fhnw.ws6c.sevensat.data.service.SatelliteService
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import org.json.JSONObject


@Composable
fun AppUI(app : SevenSatApp, activity: ComponentActivity){
  Box(contentAlignment = Alignment.Center,
      modifier         = Modifier.fillMaxSize()
  ){
      val jsonService = SatelliteService<JSONObject>()
      val stringService = SatelliteService<String>()
      val sevenSatModel = SevenSatModel(jsonService, stringService)
      val mapModel = MapModel(activity)
      MapUI(sevenSatModel, mapModel)
  }
}
