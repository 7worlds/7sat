import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.components.YoutubePlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
  scope: CoroutineScope,
  scaffoldState: BottomSheetScaffoldState,
  model: SevenSatModel
) {
  val image: Painter = painterResource(id = R.drawable.iss)
  Column(
    modifier = Modifier
      .padding(20.dp)
      .fillMaxWidth()
  ) {
      if (model.selectedSatellites.isNotEmpty()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
    )
    {
        Text(
          text = model.selectedSatellites[0].name,
          style = MaterialTheme.typography.h1
        )
      IconButtonSat(
        onClick = {
        scope.launch {
          if (scaffoldState.bottomSheetState.isCollapsed) {
            scaffoldState.bottomSheetState.expand()
          } else {
            scaffoldState.bottomSheetState.collapse()
          }
        }
      })
    {
        Icon(
          Icons.Filled.Close,
          "close",
          tint = MaterialTheme.colors.secondary
        )

      }
      }
  Spacer(modifier = Modifier.height(20.dp))
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Column (modifier = Modifier.width(150.dp)){
      Text(text = "Position", style = MaterialTheme.typography.h2)
      Text(text = model.selectedSatellites[0].orbitalData.incl.toInt().toString(), style = MaterialTheme.typography.body1)
      Text(text = model.satellitesMap[model.selectedSatellites.first()]?.longDeg().toString(), style = MaterialTheme.typography.body1)
      Spacer(modifier = Modifier.height(20.dp))

      Text(text = "Höhe", style = MaterialTheme.typography.h2)
      Text(text = model.satellitesMap[model.selectedSatellites.first()]?.altitude.toString(), style = MaterialTheme.typography.body1)
      Spacer(modifier = Modifier.height(20.dp))

      Text(text = "NoradID", style = MaterialTheme.typography.h2)
      Text(text = model.selectedSatellites[0].noradId.toString(), style = MaterialTheme.typography.body1)
    }
    Image(painter = image, contentDescription = "Bild", modifier = Modifier.width(150.dp))
  }
  Spacer(modifier = Modifier.height(20.dp))
  Text(
    text = model.selectedSatellites[0].description,
    style = MaterialTheme.typography.body1
  )
  Spacer(modifier = Modifier.height(20.dp))
//  YoutubePlayer(key = "86YLFOog4GM")
  }
    }

}
