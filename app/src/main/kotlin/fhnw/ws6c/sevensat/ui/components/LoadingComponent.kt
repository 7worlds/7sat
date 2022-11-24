import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import java.lang.Math.cos
import java.lang.Math.sin

@Preview
@Composable
fun Loading() {

  val animateFloat = remember { Animatable(0f) }
  LaunchedEffect(animateFloat) {
    animateFloat.animateTo(
      targetValue = 100f,
      animationSpec = tween(durationMillis = 1000000, easing = LinearEasing))
  }

  var angleInDegrees = (animateFloat.value * 360.0)
  val radius = 350f
  var x = -(radius * sin(Math.toRadians(angleInDegrees))).toFloat()+350f
  var y = (radius * cos(Math.toRadians(angleInDegrees))).toFloat()+350f

  val satImage = ImageBitmap.imageResource(id = R.drawable.satelitt)
  val worldImage = ImageBitmap.imageResource(id = R.drawable.world)

  Row{
    Canvas(modifier = Modifier.width(300.dp).height(300.dp)){

      drawImage(
        image = worldImage,
        topLeft = Offset(180f, 180f))

      drawImage(
        image = satImage,
        topLeft = Offset(x, y),
      )
    }

    
  }
}




@Composable
fun Sat() {
  Image(
    painter = painterResource(id = R.drawable.satelitt),
    contentDescription = null,
    modifier = Modifier
      .rotate(90f)
      .size(75.dp)
  )
}

@Composable
fun SatPosition(
  modifier: Modifier,
  xOffset: Int,
  item: @Composable () -> Unit
) {
  // outer box
  Box(modifier) {
    // inner box
    Box(
      Modifier
        .offset(x = xOffset.dp)
        .align(Alignment.CenterStart)
    ) {
      item()
    }
  }
}