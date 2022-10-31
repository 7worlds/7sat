package fhnw.ws6c.sevensat.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fhnw.ws6c.sevensat.SevenSatApp


@Composable
fun AppUI(model : SevenSatApp, activity: ComponentActivity){
  Box(contentAlignment = Alignment.Center,
      modifier         = Modifier.fillMaxSize()
  ){
      MapUI(activity)
  }
}
