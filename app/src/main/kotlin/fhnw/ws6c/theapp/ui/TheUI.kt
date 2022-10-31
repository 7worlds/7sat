package fhnw.ws6c.theapp.ui

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import fhnw.ws6c.theapp.model.TheModel


@Composable
fun AppUI(model : TheModel, activity: ComponentActivity){
  Box(contentAlignment = Alignment.Center,
      modifier         = Modifier.fillMaxSize()
  ){
      MapUI(activity)
  }
}
