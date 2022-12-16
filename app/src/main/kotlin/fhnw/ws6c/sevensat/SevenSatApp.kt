package fhnw.ws6c.sevensat

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
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
import fhnw.ws6c.sevensat.model.TWO_DAYS_IN_MILLIS
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import kotlinx.coroutines.*
import java.util.Date


object SevenSatApp : EmobaApp {
  private lateinit var model: SevenSatModel
  private lateinit var mapModel: MapModel
  private val jsonService = ApiService()
  private val stringService = TleService()

  override fun initialize(activity: ComponentActivity) {
    mapModel = MapModel(activity)
    model = SevenSatModel(jsonService, stringService)
    initSatellitesOnMap(activity)
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

  private fun initSatellitesOnMap(activity: ComponentActivity) {
    if (!model.sharedPrefsTLEsExist(activity)) {
      // no data present, load tle and display
      loadLatestTLEData(activity) { initializeAppWithCurrentTLEs(activity) }
    } else if (model.sharedPrefsTLEsAreUpToDate(activity)) {
      // data is up to date, display it
      initializeAppWithCurrentTLEs(activity)

    } else {
      // data is present but not up to date
      // display current data
      initializeAppWithCurrentTLEs(activity)
      // load new data in background
      loadLatestTLEData(activity) {
        model.readTLEsFromSharedPrefs(activity) {
          refreshSatellitesOnMap(it)
        }
      }
    }
  }

  private fun initializeAppWithCurrentTLEs(activity: ComponentActivity) {
    model.readTLEsFromSharedPrefs(activity) {
      refreshSatellitesOnMap(it)
      model.activeScreen = Screen.HOME
      mapModel.addUserPositionToMap()
      model.refreshSatellites { refreshSatellitesOnMap(it) }
    }
  }

  private fun loadLatestTLEData(activity: ComponentActivity, onLoaded: () -> Unit) {
    val backgroundJob = SupervisorJob()
    val coroutine     = CoroutineScope(backgroundJob + Dispatchers.IO)
    val tleCall       = TleCall()

    coroutine.launch {
      TleService().loadRemoteData(tleCall)
      val prefs           = activity.getSharedPreferences(activity.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
      val editor          = prefs.edit()
      val data            = tleCall.getResponse()

      editor.clear()
      data?.forEach{ entry ->
        editor.putString(entry.key.toString(), entry.value.toList().joinToString(";"))
      }
      editor.putLong(activity.getString(R.string.last_tle_sync), Date().time)
      editor.apply()
      onLoaded()
    }
  }

  private fun refreshSatellitesOnMap(satellitesMap: Map<Satellite, SatPos>) {
    mapModel.clearSatellites()
    satellitesMap.forEach { satellite ->
      mapModel.addSatellite(satellite.key, satellite.value)
    }
  }

}
