package fhnw.ws6c.sevensat.ui.theme


import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Colors = darkColors(
  //Background colors
  primary          = Color(0xFFFED428),
  background       = Color(0xFF1E1E1E),
  error            = Color(0xFFCF6679),

  //Typography and icon colors
  onPrimary        = Color(0xFF1E1E1E),
  onBackground     = Color(0xFFFED428),
  onError          = Color.Black,
)


@Composable
fun SevenSatTheme(content: @Composable() () -> Unit) {
  MaterialTheme(
    colors     = Colors,
    typography = typography,
    content    = content
  )
}