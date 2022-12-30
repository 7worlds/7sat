package fhnw.ws6c.sevensat

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.service.JsonService
import fhnw.ws6c.sevensat.model.Screen
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI
import fhnw.ws6c.sevensat.ui.components.LoadingUI
import fhnw.ws6c.sevensat.ui.theme.SevenSatTheme
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.satnogs.AllTleCall
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date


object SevenSatApp : EmobaApp {
  private lateinit var model: SevenSatModel
  private val jsonService = JsonService()
  private lateinit var mapModel: MapModel

  override fun initialize(activity: ComponentActivity) {
    mapModel = MapModel(activity)
    model = SevenSatModel(jsonService)
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
    val tleCall       = AllTleCall()

    coroutine.launch {
      JsonService().loadRemoteData(tleCall)
      val prefs   = activity.getSharedPreferences(activity.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
      val editor  = prefs.edit()
      val data    = tleCall.getResponse()
      val allTle  = data?.getJSONArray("values") as JSONArray

      val prefsSyncTime   = activity.getSharedPreferences(activity.getString(R.string.last_tle_sync), Context.MODE_PRIVATE)
      val editorSyncTime  = prefsSyncTime.edit()

      editor.clear()

      for (i in 0 until allTle.length()) {
        val tle = allTle[i] as JSONObject
        editor.putString(
          tle.getLong("norad_cat_id").toString(),
          listOf(
            tle.getString("tle0"),
            tle.getString("tle1"),
            tle.getString("tle2")
          ).joinToString(";")
        )
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
