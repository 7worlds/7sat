import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState) {
  Column (modifier = Modifier
    .padding(20.dp)
    .fillMaxWidth()) {
    Row (modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,){
      Text(text = "Name", style = MaterialTheme.typography.h1)
      IconButtonSat(onClick =  {
        scope.launch {
          if (scaffoldState.bottomSheetState.isCollapsed) {
            scaffoldState.bottomSheetState.expand()
          } else {
            scaffoldState.bottomSheetState.collapse()
          }
        }
      }) {
        Icon(
          Icons.Filled.Close,
          "close",
          tint = MaterialTheme.colors.secondary
        )

      }
    }
    Text(text = "Beschreibung", style = MaterialTheme.typography.body1)
  }
}
