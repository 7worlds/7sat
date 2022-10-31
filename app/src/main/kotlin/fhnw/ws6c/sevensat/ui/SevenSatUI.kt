package fhnw.ws6c.sevensat.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.components.Body
import fhnw.ws6c.sevensat.ui.components.Drawer
import fhnw.ws6c.sevensat.ui.theme.SevenSatTheme
import kotlinx.coroutines.launch


@Composable
fun SevenSatUI(model: SevenSatModel, activity: ComponentActivity) {
  val scaffoldState = rememberScaffoldState()
  val scope = rememberCoroutineScope()

  SevenSatTheme {
    Scaffold(
      scaffoldState = scaffoldState,
      drawerContent = { Drawer(model, scope, scaffoldState) },
      drawerBackgroundColor = MaterialTheme.colors.background,
      drawerGesturesEnabled = false,
      floatingActionButton = {
        FloatingActionButton(
          backgroundColor = MaterialTheme.colors.primary,
          onClick = {
          scope.launch {
            scaffoldState.drawerState.apply {
              if (isClosed) open() else close()
            }
          }
        }) {
          Icon(Icons.Filled.FilterList, "Filterbutton")
        }
      },
      content = {
        Column(
          modifier = Modifier.padding(it)
        ) {
          Body(model = model, activity = activity)
        }
      }


    )
  }
}