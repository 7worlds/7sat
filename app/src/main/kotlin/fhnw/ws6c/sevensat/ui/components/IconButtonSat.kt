import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonSat(onClick: () -> Unit, icon: @Composable () -> Unit) {
  IconButton(
    onClick = onClick,
    modifier = Modifier.clip(CircleShape)
      .then(Modifier.size(40.dp))
      .background(MaterialTheme.colors.secondaryVariant)
  ) {
    icon()

  }
}
