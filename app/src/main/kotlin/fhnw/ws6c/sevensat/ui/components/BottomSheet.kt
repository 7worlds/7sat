import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import fhnw.ws6c.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.ui.components.YoutubePlayer

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState, model:SevenSatModel) {
  val image: Painter = painterResource(id = R.drawable.iss)
  Column (modifier = Modifier
    .padding(20.dp)
    .fillMaxWidth()) {
    Row (modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,){
      if (model.selectedSatellites.isNotEmpty()){
      Text(text = model.selectedSatellites.get(0).name, style = MaterialTheme.typography.h1)
      }


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
    }
    Spacer(modifier = Modifier.height(20.dp))
    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
      Column {
        Text(text = "Position", style = MaterialTheme.typography.h2)
        Text(text = "lat: 23436456i34834593", style = MaterialTheme.typography.body1)
        Text(text = "long: 83745983274598235", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Höhe", style = MaterialTheme.typography.h2)
        Text(text = "418km", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Sichtbarkeit", style = MaterialTheme.typography.h2)
        Text(text = "Ja", style = MaterialTheme.typography.body1)


      }
      Image(painter = image, contentDescription = "Bild", modifier = Modifier.width(150.dp))
    }
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.", style = MaterialTheme.typography.body1)
    Spacer(modifier = Modifier.height(20.dp))
    YoutubePlayer(key = "86YLFOog4GM")
    
  }
