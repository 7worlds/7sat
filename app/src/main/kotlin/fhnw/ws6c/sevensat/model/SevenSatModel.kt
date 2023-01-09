package fhnw.ws6c.sevensat.model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.data.celestrak.CategoryCall
import fhnw.ws6c.sevensat.data.satnogs.DetailByIdCall
import fhnw.ws6c.sevensat.data.service.JsonService
import fhnw.ws6c.sevensat.model.orbitaldata.EARTH_CIRCUMFERENCE
import fhnw.ws6c.sevensat.model.orbitaldata.EARTH_RADIUS
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.*

private const val TWO_DAYS_IN_MILLIS = 172_800_000
private const val MAX_AMOUNT_OF_LINE_POINTS = 1000
private const val REFRESH_RATE = 300L

class SevenSatModel {
  private val backgroundJob   = SupervisorJob()
  private val modelScope      = CoroutineScope(backgroundJob + Dispatchers.IO)
  val mainHandler             = Handler(Looper.getMainLooper())
  val allSatellitesMap        = ConcurrentHashMap<Satellite, SatPos>()
  var filterdSatellitesMap    = ConcurrentHashMap<Satellite, SatPos>()
  val selectedSatellites      = mutableStateListOf<Satellite>()
  var observedSatellite       = -1L;

  var activeScreen by mutableStateOf(Screen.LOADING)

  /**
   * Gets Details about satelites, if user clicks on it
   */
  fun getSatelliteDetails(satellite: Satellite) {
    val detailCall = DetailByIdCall(satellite.noradId)
    val satnogsService = JsonService()
    satnogsService.loadRemoteData(detailCall)
    val sat = SatelliteBuilder()
      .withSatellite(satellite)
      .withDetails(detailCall.getResponse()!!)
      .build()
    selectedSatellites.add(0, sat)
  }

  fun refreshFlightLines(onRefreshed: (List<SatPos>) -> Unit) {
    mainHandler.post(object : Runnable {
      override fun run() {
        if(observedSatellite != -1L){
          calculateFlightLineForSatellite(observedSatellite, onRefreshed)
        }
        mainHandler.postDelayed(this, 100)
      }
    })
  }

  fun filterWithCategories(categories: List<String>) {
    val catNorads = mutableListOf<Number>()
    modelScope.launch {
      for (cat in categories) {
        val categoryCall = CategoryCall(cat)
        val celesTrakService = JsonService()
        celesTrakService.loadRemoteData(categoryCall)
        //Get new Data
        val data = categoryCall.getResponse()!!.getJSONArray("values") as JSONArray
        for (i in 0 until data.length()) {
          val obj = data[i] as JSONObject
          val noradID = (obj.getLong("NORAD_CAT_ID"))
          catNorads.add(noradID)
        }
      }
      filterdSatellitesMap.clear()

      val allNorads = allSatellitesMap.keys.map { it.noradId }
      val filterdNorads = allNorads.filter { catNorads.contains(it)}
      allSatellitesMap
        .filterKeys { filterdNorads.contains(it.noradId) }
        .forEach {
          filterdSatellitesMap[it.key] = it.value
        }
    }
  }
  fun removeFilter(){
    filterdSatellitesMap.clear()
    allSatellitesMap.forEach { filterdSatellitesMap[it.key] = it.value }
  }

  fun refreshSatellites(onRefreshed: (Map<Satellite, SatPos>) -> Unit) {
    mainHandler.post(object : Runnable {
      override fun run() {
        modelScope.run {
          filterdSatellitesMap.keys.forEach { satellite ->
            filterdSatellitesMap[satellite] = satellite.getPosition(Date().time)
          }
          onRefreshed(filterdSatellitesMap)
        }
        mainHandler.postDelayed(this, REFRESH_RATE)
      }
    })
  }

  fun sharedPrefsTLEsExist(context: Context): Boolean {
    val prefs = getTLEsFromSharedPrefs(context, R.string.last_tle_sync)
    return prefs.all.containsKey(context.getString(R.string.last_tle_sync))
  }

  fun sharedPrefsTLEsAreUpToDate(context: Context): Boolean {
    val prefs = getTLEsFromSharedPrefs(context, R.string.last_tle_sync)
    if (!prefs.all.containsKey(context.getString(R.string.last_tle_sync))) return false
    val loadingDateMillis = prefs.getLong(context.getString(R.string.last_tle_sync), 0)

    return Date().time - loadingDateMillis < TWO_DAYS_IN_MILLIS
  }

  fun readTLEsFromSharedPrefs(context: Context, onLoaded: (Map<Satellite, SatPos>) -> Unit) {
    val prefs = getTLEsFromSharedPrefs(context, R.string.tle_preferences)

    modelScope.launch {
      val satellites =
        prefs.all.entries
          .map { SatelliteBuilder().withPlainTextTleData(context, it.key.toLong()).build() }
      satellites.forEach {
        val pos = it.getPosition(Date().time)
        filterdSatellitesMap[it] =  pos
        allSatellitesMap[it] =  pos
      }
      onLoaded(allSatellitesMap)
    }
  }

  private fun getTLEsFromSharedPrefs(context: Context, filename: Int) =
    context.getSharedPreferences(
      context.getString(filename),
      Context.MODE_PRIVATE
    )

  fun calculateFlightLineForSatellite(noradId: Long, onCalculated: (List<SatPos>) -> Unit) {
    val satellite = allSatellitesMap.keys.find { it.noradId == noradId }
    if (satellite != null) {
      val calendar = Calendar.getInstance()
      calendar.time = Date()
      val points = mutableListOf<SatPos>()

      val factor = 1
      val pointCount = getAmountOfPointsForSatellites(satellite) * factor

      for (i in 0..pointCount step 1) {
        points.add(satellite.getPosition(calendar.time.time))
        calendar.add(Calendar.SECOND, 60 / factor)
      }
      onCalculated(points)
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
    val numberOfPoints = (EARTH_CIRCUMFERENCE / (2 * kmIn1Min)).roundToInt()
    return if (numberOfPoints > MAX_AMOUNT_OF_LINE_POINTS) MAX_AMOUNT_OF_LINE_POINTS else numberOfPoints
  }


  /**
   * calcultes the distance between two points in km.
   */
  private fun haversine(point1: SatPos, point2: SatPos): Double {
    point1.latitude - point2.latitude
    val dLat = deg2rad(point2.latitude - point1.latitude)  // deg2rad below
    val dLon = deg2rad(point2.longitude - point1.longitude)
    val a =
      sin(dLat / 2) * sin(dLat / 2) +
          cos(deg2rad(point1.latitude)) * cos(point2.latitude) *
          sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return EARTH_RADIUS * c; // Distance in km
  }

  private fun deg2rad(x: Double) = x

}