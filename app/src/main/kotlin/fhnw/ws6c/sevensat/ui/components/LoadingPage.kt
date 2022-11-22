package fhnw.ws6c.sevensat.ui.components

import Loading
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.Screen
import fhnw.ws6c.sevensat.model.SevenSatModel

@Composable
fun LoadingUI(model: SevenSatModel) {
  Box (modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),)
   {
    Column(modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
      Image(painter = painterResource(id = R.drawable.logo), contentDescription = "welt", modifier = Modifier.height(40.dp))
      Loading()
    }

    Row (modifier = Modifier.fillMaxWidth().padding(0.dp,0.dp,0.dp,30.dp).align(Alignment.BottomEnd),
      verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly){
      Text(text = "Sateliten werden in den Himmer katapultiert", color = MaterialTheme.colors.secondary, style = MaterialTheme.typography.h2)
      Button(
        onClick = { model.activescreen = Screen.HOME }
      ) {
        Text(text = "weiter", color = MaterialTheme.colors.background, style = MaterialTheme.typography.h2)

      }
    }
  }

}