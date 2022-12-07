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
import kotlin.math.*

class SevenSatModel(
  private val jsonService: Service<JSONObject>,
  val stringService: Service<Map<Long, Triple<String, String, String>>>
) {
  private val backgroundJob = SupervisorJob()
  private val modelScope    = CoroutineScope(backgroundJob + Dispatchers.IO)
  val mainHandler           = Handler(Looper.getMainLooper())
  val satellitesMap         = mutableStateMapOf<Satellite, SatPos>()
  val selectedSatellites    = mutableStateListOf<Satellite>()
  var activeScreen by mutableStateOf(Screen.HOME)

  val clickedSatelliteRoute = mutableStateListOf<SatPos>()

  fun refreshSatellites() {
    mainHandler.post(object : Runnable {
      override fun run() {
        modelScope.run {
          satellitesMap.keys.forEach { satellite ->
            satellitesMap[satellite] = satellite.getPosition(Date().time)
          }
        }
        mainHandler.postDelayed(this, 5000)
      }
    })
  }

  fun loadSatellites(context: Context) {
    val prefs = context.getSharedPreferences(
      context.getString(R.string.tle_preferences),
      Context.MODE_PRIVATE
    )

    modelScope.launch {
    val satellites =
      prefs.all.entries.take(50)//.filter { it.key.equals("43556") || it.key.equals("25544") }
        .map { SatelliteBuilder().withPlainTextTleData(context, it.key.toLong()).build() }
      satellites.forEach {
        satellitesMap[it] = it.getPosition(Date().time)
      }
    }
  }

  fun calculateFlightLineForSatellite(noradId: Long) {
    modelScope.launch {
      clickedSatelliteRoute.clear()
      val satellite = satellitesMap.keys.find { it.noradId == noradId }
      if (satellite != null) {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        val points = mutableListOf<SatPos>()
        val start = System.currentTimeMillis()
        satellite.getPosition(calendar.time.time)

        val step = 1
        val pointCount = getAmountOfPointsForSatellites(satellite) / step

        for (i in 0..pointCount step step) {
          calendar.add(Calendar.MINUTE, step)
          points.add(satellite.getPosition(calendar.time.time))
        }

        val end = System.currentTimeMillis()
        println("it took ${(end - start) / 1000} seconds")
        clickedSatelliteRoute.addAll(points)
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
