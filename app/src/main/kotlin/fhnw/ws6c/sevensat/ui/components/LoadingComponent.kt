import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R

@Composable
fun Loading() {
  val size = 200.dp;
  var circlePositionState by remember { mutableStateOf((0.5f)) }
  val circlePosition by animateFloatAsState(
    targetValue = circlePositionState,
  spring(
    Spring.DampingRatioHighBouncy
  )
  )

  Box(
    modifier = Modifier
      .size(size, size)
      .border(BorderStroke(2.dp, MaterialTheme.colors.primary))
  )
  {
    Box(modifier = Modifier.align(Alignment.Center)){
    Image(painter = painterResource(id = R.drawable.world), contentDescription = "welt", modifier = Modifier.height(size.minus(80.dp)))
      Box(modifier = Modifier.align(Alignment.Center)) {
        Text(
          text = "Hier custom Drawer",
          color = MaterialTheme.colors.background,
          textAlign = TextAlign.Center,
        )
      }

   }
//    Button(onClick = { circlePositionState += 0.1f}) {
//      Text(text = "drehen")
//    }
    CircularProgressIndicator( modifier = Modifier
      .align(Alignment.Center)
      .size(size.minus(20.dp)));
    Box(modifier = Modifier
      .align(Alignment.Center)
      .size(size.minus(20.dp))
      .border(BorderStroke(2.dp, MaterialTheme.colors.secondary))){
      Image(painter = painterResource(id = R.drawable.satelitt), contentDescription = "Sat", modifier = Modifier.size(20.dp))
    }
    
  }
}
