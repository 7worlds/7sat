package fhnw.ws6c.sevensat.ui.components

import IconButtonSat
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
public fun Drawer(model: SevenSatModel, scope: CoroutineScope, scaffoldState: ScaffoldState) {
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
          icon = { Icon(Icons.Filled.Close, "schliessen", tint = MaterialTheme.colors.secondary)}
        )

      }
    }
  }
}