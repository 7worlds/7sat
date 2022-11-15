package fhnw.ws6c.sevensat.ui

import BottomSheet
import IconButtonSat
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.SevenSatApp
import fhnw.ws6c.sevensat.data.service.SatelliteService
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.components.Drawer
import fhnw.ws6c.sevensat.ui.theme.SevenSatTheme
import kotlinx.coroutines.launch
import org.json.JSONObject


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SevenSatUI(model: SevenSatModel, activity: ComponentActivity) {
  val mapModel = MapModel(activity)
  val scope = rememberCoroutineScope()
  val scaffoldState = rememberBottomSheetScaffoldState(
    bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
  )

  SevenSatTheme {
    BottomSheetScaffold(
      scaffoldState = scaffoldState,
      drawerContent = { Drawer(model, scope, scaffoldState) },
      drawerBackgroundColor = MaterialTheme.colors.background,
      drawerGesturesEnabled = false,
           sheetContent = { BottomSheet(scope, scaffoldState, model) },
      sheetGesturesEnabled = true,
      sheetContentColor = MaterialTheme.colors.secondary,
      sheetBackgroundColor = MaterialTheme.colors.background,
      //sheetElevation = 300.dp,
      sheetPeekHeight = 0.dp,
      sheetShape = RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp),
      content = {

        Box(modifier = Modifier.padding(3.dp)) {
          Column(
            modifier = Modifier.padding(it)
          ) {

            MapUI(model, mapModel,scope, scaffoldState)
          }

//          Column(modifier = Modifier.padding(10.dp, 30.dp)) {
//            IconButtonSat(
//              onClick = {
//                scope.launch {
//                  scaffoldState.drawerState.apply {
//                    if (isClosed) open() else close()
//                  }
//                }
//              },
//              icon = {
//                Icon(
//                  Icons.Filled.FilterAlt,
//                  "Filterbutton",
//                  tint = MaterialTheme.colors.secondary
//                )
//              }
//            )
//          }
        }
      }
    )
  }
}