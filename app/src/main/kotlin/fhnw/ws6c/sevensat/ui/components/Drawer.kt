package fhnw.ws6c.sevensat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.sevensat.model.SevenSatModel

@Composable
public fun Drawer(model: SevenSatModel) {
  with(model) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
        text = "Filter",
        style = MaterialTheme.typography.h2

      )

    }
  }
}