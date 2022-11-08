import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState) {
  val image: Painter = painterResource(id = R.drawable.bob)
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
    Spacer(modifier = Modifier.height(20.dp))
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
      Column {
        Text(text = "Position", style = MaterialTheme.typography.h2)
        Text(text = "lat", style = MaterialTheme.typography.body1)
        Text(text = "long", style = MaterialTheme.typography.body1)

      }
      Image(painter = image, contentDescription = "Bild")
    }
    Text(text = "Beschreibung", style = MaterialTheme.typography.body1)
  }
}
