package fhnw.ws6c.sevensat.ui.components

import Loading
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.Screen
import fhnw.ws6c.sevensat.model.SevenSatModel
import kotlinx.coroutines.delay

@Composable
fun LoadingUI(model: SevenSatModel) {
  var radioMessages = arrayListOf(
    "'Loading'",
    "'ISS - can you hear me? - over'",
    "'Loud an clearly commander - over'",
    "'We are sending you all of our Space data right now.- over'",
    "'Roger that, data is being prepared - over and out'",
    "---"
  )
  var activeText by remember { mutableStateOf(0) };


  LaunchedEffect(Unit) {
    while( activeText< radioMessages.size-1) {
      activeText +=1;
      delay(5000)
    }
  }

  Box (modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),)
   {
    Column(modifier = Modifier
      .fillMaxHeight()
      .fillMaxWidth(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally) {
      Image(painter = painterResource(id = R.drawable.logo), contentDescription = "7SAT logo", modifier = Modifier.size(150.dp).clickable { model.activeScreen = Screen.HOME  })
      Loading()
    }

    Column (modifier = Modifier.fillMaxWidth().padding(0.dp,0.dp,0.dp,30.dp).align(Alignment.BottomEnd),
      verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
      Text(text = radioMessages[activeText], style = MaterialTheme.typography.h6)
    }
  }

}

@Composable
fun HelloContent(name: String, onNameChange: (String) -> Unit) {
  Column(modifier = Modifier.padding(16.dp)) {
    Text(
      text = "Hello, $name",
      modifier = Modifier.padding(bottom = 8.dp),
      style = MaterialTheme.typography.h5
    )
  }
}