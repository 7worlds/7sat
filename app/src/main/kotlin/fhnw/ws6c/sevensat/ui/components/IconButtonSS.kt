import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IconButtonSS(onClick: () -> Unit, icon: Unit, color: androidx.compose.ui.graphics.Color) {
 IconButton(
   onClick = onClick,
   modifier = Modifier.padding(2.dp),



 ) {
   icon
 }

}
