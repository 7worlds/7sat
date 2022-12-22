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
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import kotlinx.coroutines.*
import java.util.Date


object SevenSatApp : EmobaApp {
  private lateinit var model: SevenSatModel
  private val jsonService = JsonService()
  private val stringService = PlainTextService()
  private lateinit var mapModel: MapModel

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
      model.refreshSatellites { satellites -> refreshSatellitesOnMap(satellites) }
    }
  }

  private fun loadLatestTLEData(activity: ComponentActivity, onLoaded: () -> Unit) {
    val backgroundJob = SupervisorJob()
    val coroutine     = CoroutineScope(backgroundJob + Dispatchers.IO)
    val tleCall       = TleAllCall()

    coroutine.launch {
      PlainTextService().loadRemoteData(tleCall)
      val prefs           = activity.getSharedPreferences(activity.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
      val editor          = prefs.edit()
      val data            = tleCall.getResponse()

      val prefsSyncTime   = activity.getSharedPreferences(activity.getString(R.string.last_tle_sync), Context.MODE_PRIVATE)
      val editorSyncTime  = prefsSyncTime.edit()

      editor.clear()
      data?.forEach{ entry ->
        editor.putString(entry.key.toString(), entry.value.toList().joinToString(";"))
      }
      editor.apply()

      editorSyncTime.clear()
      editorSyncTime.putLong(activity.getString(R.string.last_tle_sync), Date().time)
      editorSyncTime.apply()

      onLoaded()
    }
  }

  private fun refreshSatellitesOnMap(satellitesMap: Map<Satellite, SatPos>) =
    mapModel.refreshSatellitePositionOnMap(satellitesMap)
}
