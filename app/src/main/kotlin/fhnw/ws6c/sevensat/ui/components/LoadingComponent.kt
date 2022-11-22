import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R

@Composable
fun Loading() {
  val size = 200.dp;

  Box(
    modifier = Modifier
      .size(size, size)
      .background(MaterialTheme.colors.primary)
  )
  {
    Image(painter = painterResource(id = R.drawable.world), contentDescription = "welt")
    Text(text = "Hier custom Drawer", color = MaterialTheme.colors.background,  textAlign = TextAlign.Center,)
  }
}
