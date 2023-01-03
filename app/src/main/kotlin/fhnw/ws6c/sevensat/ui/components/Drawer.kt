package fhnw.ws6c.sevensat.ui.components

import IconButtonSat
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Drawer(scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState, model: SevenSatModel) {
  var sliderPosition by remember { mutableStateOf(0f) }
  val aboveState = remember { mutableStateOf(false) }
  val issChecked = remember { mutableStateOf(false) }
  Column(modifier = Modifier
    .padding(20.dp)
    .fillMaxWidth()) {
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
        icon = { Icon(Icons.Outlined.Close,
          "schliessen", tint = MaterialTheme.colors.secondary)}
      )
    }
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = "Schnellfilter", style= MaterialTheme.typography.h3)
    Row (verticalAlignment = Alignment.CenterVertically){
      Checkbox(  checked = issChecked.value,
        onCheckedChange = {
          issChecked.value = it
          model.satellitesMap.filter { it.key.noradId == 25544 }
        })
      Text(text = "ISS", style= MaterialTheme.typography.body1)
    }
    Row (verticalAlignment = Alignment.CenterVertically){
      Checkbox(  checked = aboveState.value,
        onCheckedChange = {
          aboveState.value = it
          //TODO
        })
      Text(text = "Above", style= MaterialTheme.typography.body1)
    }
    Spacer(modifier = Modifier.height(20.dp) )
    Text(text = "Distanzfilter", style= MaterialTheme.typography.h3)

    Slider(
      value = 1F,
      onValueChange = { sliderPosition = it },
      //onValueChangeFinished = {TODO},
      modifier = Modifier.fillMaxWidth())

  }
}