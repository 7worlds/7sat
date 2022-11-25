package fhnw.ws6c.sevensat.model

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.*
import com.mapbox.maps.MapView
import fhnw.ws6c.sevensat.data.n2yo.TleCall
import fhnw.ws6c.sevensat.data.service.Service
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos
import fhnw.ws6c.sevensat.model.satellite.Satellite
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class SevenSatModel(
  private val jsonService: Service<JSONObject>,
  val stringService: Service<Map<Long, Triple<String, String, String>>>
) {
  private val backgroundJob = SupervisorJob()
  private val modelScope = CoroutineScope(backgroundJob + Dispatchers.IO)
  val mainHandler = Handler(Looper.getMainLooper())
  val satellitesMap = mutableStateMapOf<Satellite, SatPos>()
  val selectedSatellites = mutableStateListOf<Satellite>()
  var activeScreen by mutableStateOf(Screen.LOADING)

  val clickedSatelliteRoute = mutableStateListOf<SatPos>()

  fun refreshSatellites() {
    mainHandler.post(object : Runnable {
      override fun run() {
        satellitesMap.keys.forEach { satellite ->
          satellitesMap[satellite] = satellite.getPosition(Date().time)
        }
        mainHandler.postDelayed(this, 10000)
      }
    })
  }

  fun loadSatellites(mapView: MapView) {
    val tleCall = TleCall(25544)
    modelScope.launch {
      jsonService.loadRemoteData(tleCall);
      val sat = SatelliteBuilder()
        .withTleJsonData(tleCall.getResponse()!!).build()
      satellitesMap[sat] = sat.getPosition(Date().time)
      calculateISSLine()
    }
  }

  private fun calculateISSLine() {
    val iss = satellitesMap.keys.find { it.noradId == 25544L }
    if (iss != null) {
      val calendar = Calendar.getInstance()
      calendar.time = Date()
      val points = mutableListOf<SatPos>()
      val start = System.currentTimeMillis()
      for (i in 0..90 step 5) {
        calendar.add(Calendar.MINUTE, 5)
        points.add(iss.getPosition(calendar.time.time))
      }
      val end = System.currentTimeMillis()
      println(end - start)
      clickedSatelliteRoute.addAll(points)
    }
  }
}