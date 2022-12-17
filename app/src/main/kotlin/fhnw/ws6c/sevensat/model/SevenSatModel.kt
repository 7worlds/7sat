package fhnw.ws6c.sevensat.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.service.Service
import fhnw.ws6c.sevensat.model.orbitaldata.EARTH_CIRCUMFERENCE
import fhnw.ws6c.sevensat.model.orbitaldata.EARTH_RADIUS
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*

const val TWO_DAYS_IN_MILLIS = 172_800_000

class SevenSatModel(
  private val jsonService: Service<JSONObject>,
  val stringService: Service<Map<Long, Triple<String, String, String>>>
) {
  private val backgroundJob = SupervisorJob()
  private val modelScope    = CoroutineScope(backgroundJob + Dispatchers.IO)
  val mainHandler           = Handler(Looper.getMainLooper())
  val satellitesMap         = ConcurrentHashMap<Satellite, SatPos>()
  val selectedSatellites    = mutableStateListOf<Satellite>()
  var activeScreen by mutableStateOf(Screen.LOADING)


  fun refreshSatellites(onRefreshed: (Map<Satellite, SatPos>) -> Unit) {
    mainHandler.post(object : Runnable {
      override fun run() {
        modelScope.run {
          satellitesMap.keys.forEach { satellite ->
            satellitesMap[satellite] = satellite.getPosition(Date().time)
          }
          onRefreshed(satellitesMap)
        }
        mainHandler.postDelayed(this, 5000)
      }
    })
  }

  fun sharedPrefsTLEsExist(context: Context): Boolean{
    val prefs = getTLEsFromSharedPrefs(context)
    return prefs.all.containsKey(context.getString(R.string.last_tle_sync))
  }

  fun sharedPrefsTLEsAreUpToDate(context: Context): Boolean {
    val prefs = getTLEsFromSharedPrefs(context)
    if (!prefs.all.containsKey(context.getString(R.string.last_tle_sync))) return false
    val loadingDateMillis = prefs.getLong(context.getString(R.string.last_tle_sync), 0)

    return Date().time - loadingDateMillis < TWO_DAYS_IN_MILLIS
  }

  fun readTLEsFromSharedPrefs(context: Context, onLoaded: (Map<Satellite, SatPos>) -> Unit) {
    val prefs = getTLEsFromSharedPrefs(context)

    modelScope.launch {
      val satellites =
        prefs.all.entries.take(50)//.filter { it.key.equals("43556") || it.key.equals("25544") }
          .map { SatelliteBuilder().withPlainTextTleData(context, it.key.toLong()).build() }
      satellites.forEach {
        satellitesMap[it] = it.getPosition(Date().time)
      }
      onLoaded(satellitesMap)
    }
  }

  private fun getTLEsFromSharedPrefs(context: Context) =
    context.getSharedPreferences(
      context.getString(R.string.tle_preferences),
      Context.MODE_PRIVATE
    )

  fun calculateFlightLineForSatellite(noradId: Long, onCalculated: (List<SatPos>) -> Unit) {
    modelScope.launch {
      val satellite = satellitesMap.keys.find { it.noradId == noradId }
      if (satellite != null) {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val points = mutableListOf<SatPos>()
        val start = System.currentTimeMillis()
        satellite.getPosition(calendar.time.time)

        val factor = 1
        val pointCount = getAmountOfPointsForSatellites(satellite) * factor

        for (i in 0..pointCount step 1) {
          calendar.add(Calendar.SECOND, 60 / factor)
          points.add(satellite.getPosition(calendar.time.time))
        }
        onCalculated(points)
        val end = System.currentTimeMillis()
        println("it took ${(end - start) / 1000} seconds")
      }
    }
  }

  /**
   * how many points are needed to add a point all "minutes"-minute to orbit half of the earth?
   */
  private fun getAmountOfPointsForSatellites(satellite: Satellite): Int {
    val calendar = Calendar.getInstance()
    val positionNow = satellite.getPosition(calendar.time.time)
    calendar.add(Calendar.MINUTE, 1)
    val positionIn1Min = satellite.getPosition(calendar.time.time)

    val kmIn1Min = haversine(positionNow, positionIn1Min)
    return (EARTH_CIRCUMFERENCE / (2 * kmIn1Min)).roundToInt()
  }


  /**
   * calcultes the distance between two points in km.
   */
  private fun haversine(point1: SatPos, point2: SatPos): Double {
    point1.latitude - point2.latitude
    val dLat = deg2rad(point2.latitude - point1.latitude)  // deg2rad below
    val dLon = deg2rad(point2.longitude - point1.longitude)
    val a =
      sin(dLat/2) * sin(dLat/2) +
          cos(deg2rad(point1.latitude)) * cos(point2.latitude) *
          sin(dLon/2) * sin(dLon/2)
    val c = 2 * atan2(sqrt(a), sqrt(1-a))
    return  EARTH_RADIUS * c; // Distance in km
  }

  private fun deg2rad(x: Double) = x
}
