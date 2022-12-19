package fhnw.ws6c.sevensat.model.satellite

import android.content.Context
import androidx.activity.ComponentActivity
import fhnw.ws6c.R
import fhnw.ws6c.sevensat.model.orbitaldata.OrbitalData
import fhnw.ws6c.sevensat.util.tle.TLEParser
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SatelliteBuilder {
  var noradId:      Long   = -1
  var name:         String = ""
  var description:  String = ""
  var image:        String = ""
  var tleLine1:     String = ""
  var tleLine2:     String = ""
  var coordinates:  Map<Long, Triple<Double, Double, Double>> = Collections.emptyMap() // lat, lng, alt
  var orbitalData: OrbitalData? = null

  fun withN2yoTleJsonData(jsonObject: JSONObject) : SatelliteBuilder {
    val info  = jsonObject.getJSONObject("info")
    noradId   = info.getLong  ("satid")
    name      = info.getString("satname")

    val tleLines = jsonObject.getString("tle").split("\n")
    tleLine1  = tleLines[0].dropLast(1) // remove "\r" at the end of the string
    tleLine2  = tleLines[1]

    val tleParser = TLEParser()
    // TODO: what todo if tle is empty or invalid?
    orbitalData = tleParser.parseSingleTLE(name, tleLine1, tleLine2)
    return this
  }

  fun withPlainTextTleData(activity: Context, id: Long = noradId) : SatelliteBuilder {
    noradId = id
    val prefs   = activity.getSharedPreferences(activity.getString(R.string.tle_preferences), Context.MODE_PRIVATE)
    val tleData = prefs.getString(id.toString(), "")
    val lines = tleData?.split(";")
    tleLine1 = lines?.get(1) ?: ""
    tleLine2 = lines?.get(2) ?: ""
    val tleParser = TLEParser()
    orbitalData = tleParser.parseSingleTLE(name, tleLine1, tleLine2)
    return this
  }

  fun withDescription(satDescription: String) : SatelliteBuilder {
    description = satDescription
    return this
  }

  fun withPositionData(jsonObject: JSONObject) : SatelliteBuilder {
    val info  = jsonObject.getJSONObject("info")
    noradId   = info.getLong  ("satid")
    name      = info.getString("satname")
    val positions = jsonObject.getJSONArray("positions").getJSONObject(0)
    val triple = Triple ( positions.getDouble("satlatitude"),
                          positions.getDouble("satlongitude"),
                          positions.getDouble("sataltitude"))

    coordinates = mapOf(noradId to triple)
    return this
  }

  fun withDetails(jsonObject: JSONObject): SatelliteBuilder {
    try {
      val data = jsonObject.getJSONArray("values")[0] as JSONObject
      image = data.getString("image")
    } catch (jsonException: JSONException) {
      System.err.println("Couldn't parse json data! " + jsonException.message)
    }
    return this;
  }

  fun build() : Satellite {
      return Satellite(this)
  }
}