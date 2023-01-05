import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(
  scope: CoroutineScope, scaffoldState: BottomSheetScaffoldState, model: SevenSatModel
) {
  val uriHandler = LocalUriHandler.current
  Column(
    modifier = Modifier
      .padding(20.dp)
      .fillMaxWidth()
  ) {
    if (model.selectedSatellites.isNotEmpty()) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Column {
          Text(
            text = model.selectedSatellites[0].name, style = MaterialTheme.typography.h1
          )
          Text(
            text = "Norad-ID: " + model.selectedSatellites[0].noradId.toString(),
            style = MaterialTheme.typography.body1
          )
        }
        IconButtonSat(
          backgroundColor = MaterialTheme.colors.onSurface,
          onClick = {
          scope.launch {
            if (scaffoldState.bottomSheetState.isCollapsed) {
              scaffoldState.bottomSheetState.expand()
            } else {
              scaffoldState.bottomSheetState.collapse()
            }
          }
        },
        icon = {
          Icon(
            Icons.Filled.Close, "close",
            tint = MaterialTheme.colors.secondary
          )

        })
      }
      Spacer(modifier = Modifier.height(20.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(modifier = Modifier.width(150.dp)) {

          if (model.selectedSatellites[0].countries.isNotEmpty()) {
            Text(text = "Countries", style = MaterialTheme.typography.body1)
            Text(text = model.selectedSatellites[0].countries, style = MaterialTheme.typography.h3)
            Spacer(modifier = Modifier.height(20.dp))
          }

          Text(text = "Flight altitude", style = MaterialTheme.typography.body1)
          Text(
            text = model.selectedSatellites[0].getPosition(Date().time).altitude.toInt()
              .toString() + " km", style = MaterialTheme.typography.h3
          )
          Spacer(modifier = Modifier.height(20.dp))

          if (!model.selectedSatellites[0].launched.isEqual(ZonedDateTime.parse("1001-01-28T00:00:00Z"))) {
            Text(text = "Launched", style = MaterialTheme.typography.body1)
            Text(text = model.selectedSatellites[0].launched.year.toString(), style = MaterialTheme.typography.h3)
            Spacer(modifier = Modifier.height(20.dp))
          }


          if (model.selectedSatellites[0].website.isNotEmpty()) {
            Text(text = "Website", style = MaterialTheme.typography.body1)
            ClickableText(text = buildAnnotatedString {
              pushStyle(SpanStyle(color = MaterialTheme.colors.secondary))
              append(model.selectedSatellites[0].website)
              toAnnotatedString()
            } , onClick = {
              uriHandler.openUri(model.selectedSatellites[0].website)
            })
          }
        }
        AsyncImage(
          model = model.selectedSatellites[0].image,
          contentDescription = "Satellite Image",
          modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .padding(20.dp, 0.dp, 0.dp, 0.dp),
          error = painterResource(R.drawable.noimage),
          placeholder = painterResource(R.drawable.loading),
          alignment = Alignment.TopCenter
        )
      }


    }
  }

}
