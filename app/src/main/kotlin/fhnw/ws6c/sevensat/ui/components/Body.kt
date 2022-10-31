package fhnw.ws6c.sevensat.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.MapUI


@Composable
fun Body(model: SevenSatModel, activity: ComponentActivity) {
  with(model)  {

    Box(contentAlignment = Alignment.Center,
      modifier         = Modifier.fillMaxSize(),
    ){
      MapUI(activity)
    }
  }
}
