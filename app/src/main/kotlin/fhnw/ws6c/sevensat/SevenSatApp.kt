package fhnw.ws6c.sevensat

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.celestrak.TleAllCall
import fhnw.ws6c.sevensat.data.service.JsonService
import fhnw.ws6c.sevensat.data.service.PlainTextService
import fhnw.ws6c.sevensat.model.Screen
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI
import fhnw.ws6c.sevensat.ui.components.LoadingUI
import fhnw.ws6c.sevensat.ui.theme.SevenSatTheme
import fhnw.ws6c.R
import kotlinx.coroutines.*


object SevenSatApp : EmobaApp {
  private lateinit var model: SevenSatModel
  private val jsonService = JsonService()
  private val stringService = PlainTextService()

  override fun initialize(activity: ComponentActivity) {
    model = SevenSatModel(jsonService, stringService)
    loadTleAsync(activity)
  }

  private fun loadTleAsync(activity: ComponentActivity) {
    val backgroundJob = SupervisorJob()
    val coroutine     = CoroutineScope(backgroundJob + Dispatchers.IO)
    val tleCall       = TleAllCall()

    coroutine.launch {
      PlainTextService().loadRemoteData(tleCall)
      val prefs   = activity.getSharedPreferences(activity.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
      val editor  = prefs.edit()
      val data    = tleCall.getResponse()

      editor.clear()
      data?.forEach{ entry ->
        editor.putString(entry.key.toString(), entry.value.toList().joinToString(";"))
      }
      editor.apply()
    }
  }

  @Composable
  override fun CreateUI(activity: ComponentActivity) {
    val mapModel = MapModel(activity)
    SevenSatTheme {
      Crossfade(targetState = model.activeScreen) { screen ->
        when (screen) {
          Screen.HOME     -> SevenSatUI(model = model, mapModel)
          Screen.LOADING  -> LoadingUI(model = model)
        }
      }
    }
  }
}
