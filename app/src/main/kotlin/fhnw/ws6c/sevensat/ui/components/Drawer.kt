package fhnw.ws6c.sevensat.ui.components

import IconButtonSat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState, model: SevenSatModel) {

  //Directfilter
  val starlink = remember { mutableStateOf(false) }
  val spaceStations = remember { mutableStateOf(false) }
  val brightest = remember { mutableStateOf(false) }
  val recentlyLaunched = remember { mutableStateOf(false) }
  val weather = remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .padding(20.dp)
      .fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
  ) {
    Column {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "Filter",
          style = MaterialTheme.typography.h1
        )
        IconButtonSat(
          backgroundColor = MaterialTheme.colors.onSurface,
          onClick = {
            scope.launch {
              scaffoldState.drawerState.apply {
                if (isClosed) open() else close()
              }
            }
          },
          icon = {
            Icon(
              Icons.Outlined.Close,
              "schliessen", tint = MaterialTheme.colors.secondary
            )
          }
        )
      }
      Spacer(modifier = Modifier.height(20.dp))
      Text(text = "Schnellfilter", style = MaterialTheme.typography.h3)

      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = spaceStations.value,
          onCheckedChange = {
            spaceStations.value = it
            if (spaceStations.value) {
              model.filterWithCategory("stations")
            } else {//TODO
            }
          })
        Text(text = "Space Stations", style = MaterialTheme.typography.body1)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = starlink.value,
          onCheckedChange = {
            starlink.value = it
            if (starlink.value) {
              model.filterWithCategory("starlink")
            } else {//TODO
            }
          })
        Text(text = "Starlink", style = MaterialTheme.typography.body1)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = brightest.value,
          onCheckedChange = {
            brightest.value = it
            if (brightest.value) {
              model.filterWithCategory("visual")
            } else {//TODO
            }
          })
        Text(text = "Brightest", style = MaterialTheme.typography.body1)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = weather.value,
          onCheckedChange = {
            weather.value = it
            if (weather.value) {
              model.filterWithCategory("weather")
            } else {//TODO
            }
          })
        Text(text = "Weather", style = MaterialTheme.typography.body1)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = recentlyLaunched.value,
          onCheckedChange = {
            recentlyLaunched.value = it
            if (recentlyLaunched.value) {
              model.filterWithCategory("last-30-days")
            } else {//TODO
            }
          })
        Text(text = "Last 30 days' launched", style = MaterialTheme.typography.body1)
      }
      Spacer(modifier = Modifier.height(20.dp))
    }
    Column() {

      Button(onClick = {
        scope.launch {
          scaffoldState.drawerState.apply {
            if (isClosed) open() else close()
          }
        }
      }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "save and explore")

      }
      Button(colors = ButtonDefaults.buttonColors(
        backgroundColor = Color.Transparent
      ),
        border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
        onClick = {
          scope.launch {
            recentlyLaunched.value = false
            weather.value = false
            brightest.value = false
            spaceStations.value = false
            starlink.value = false
            model.removeFilter()
            scaffoldState.drawerState.apply {
              if (isClosed) open() else close()
            }
          }
        }, modifier = Modifier
          .fillMaxWidth()
          .background(Color.Transparent)
      ) {
        Text(text = "clear all Filter", style = TextStyle(MaterialTheme.colors.secondary))

      }
    }

  }
}