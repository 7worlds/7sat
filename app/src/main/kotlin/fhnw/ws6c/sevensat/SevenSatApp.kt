package fhnw.ws6c.sevensat

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.celestrak.TleCall
import fhnw.ws6c.sevensat.data.service.ApiService
import fhnw.ws6c.sevensat.data.service.TleService
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
  private lateinit var mapModel: MapModel
  private val jsonService = ApiService()
  private val stringService = TleService()

  override fun initialize(activity: ComponentActivity) {
    mapModel = MapModel(activity)
    model = SevenSatModel(jsonService, stringService)
    mapModel.addUserPositionToMap()
    model.loadSatellites(activity)
    model.refreshSatellites {
      mapModel.clearSatellites()
      it.forEach { satellite ->
        mapModel.addSatellite(satellite.key, satellite.value)
      }
    }
//    loadTleAsync(activity)
  }

  private fun loadTleAsync(activity: ComponentActivity) {
    val backgroundJob = SupervisorJob()
    val coroutine     = CoroutineScope(backgroundJob + Dispatchers.IO)
    val tleCall       = TleCall()

    coroutine.launch {
      TleService().loadRemoteData(tleCall)
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
