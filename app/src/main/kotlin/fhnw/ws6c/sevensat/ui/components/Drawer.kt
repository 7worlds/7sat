package fhnw.ws6c.sevensat.ui.components

import IconButtonSat
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
public fun Drawer(model: SevenSatModel, scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState) {
  val checkedState = remember { mutableStateOf(false) }
  with(model) {
    Column(modifier = Modifier
      .padding(20.dp)
      .fillMaxWidth()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = "Filter",
          style = MaterialTheme.typography.h4
        )
        IconButtonSat(
          onClick = {
            scope.launch {
              scaffoldState.drawerState.apply {
                if (isClosed) open() else close()
              }
            }
          },
          icon = { Icon(Icons.Outlined.Close, "schliessen", tint = MaterialTheme.colors.secondary)}
        )
      }
      Row (verticalAlignment = Alignment.CenterVertically){
        Checkbox(  checked = checkedState.value,
          onCheckedChange = { checkedState.value = it })
        Text(text = "ISS", style= MaterialTheme.typography.h2)
      }
    }
  }
}