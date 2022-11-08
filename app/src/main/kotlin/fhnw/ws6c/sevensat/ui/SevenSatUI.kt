package fhnw.ws6c.sevensat.ui

import IconButtonSat
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel
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
      floatingActionButtonPosition = FabPosition.Center,

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
        Box(modifier = Modifier.padding(3.dp)) {
          Column(
            modifier = Modifier.padding(it)
          ) {
            MapUI(activity)
          }

          Column (modifier = Modifier.padding(10.dp, 30.dp)){

            IconButtonSat(
              onClick = {
                scope.launch {
                  scaffoldState.drawerState.apply {
                    if (isClosed) open() else close()
                  }
                }
              },
              icon = {
                Icon(
                  Icons.Filled.FilterAlt,
                  "Filterbutton",
                  tint = MaterialTheme.colors.secondary
                )
              }
            )
          }

        }

      }


    )
  }
}