import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R

@Composable
fun Loading() {

  val animateFloat = remember { Animatable(0f) }
  LaunchedEffect(animateFloat) {
    animateFloat.animateTo(
      targetValue = 100f,
      animationSpec = tween(durationMillis = 2000000, easing = LinearEasing))
  }

  var angleInDegrees = (animateFloat.value * 360.0)

  val satImage = ImageBitmap.imageResource(id = R.drawable.satelitt,)

  Box()
  {
    Image(painter = painterResource(id = R.drawable.world), contentDescription = "welt", modifier = Modifier.size(170.dp).align(Alignment.Center))
    Canvas(modifier = Modifier.width(220.dp).height(250.dp).rotate(angleInDegrees.toFloat() ).align(Alignment.Center)){
      drawImage(
        image = satImage,
      )
    }

    
  }
}
