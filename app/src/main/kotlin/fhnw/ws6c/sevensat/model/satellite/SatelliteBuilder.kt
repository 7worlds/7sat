package fhnw.ws6c.sevensat.model.satellite

import org.json.JSONObject
import java.util.*

class SatelliteBuilder {

  var noradId:      Long   = -1
  var name:         String = ""
  var description:  String = ""
  var tleLine1:     String = ""
  var tleLine2:     String = ""
  var coordinates:  Map<Long, Triple<Double, Double, Double>> = Collections.emptyMap() // lat, lng, alt

  fun withTleJsonData(jsonObject: JSONObject) : SatelliteBuilder{
    val info  = jsonObject.getJSONObject("info")
    noradId   = info.getLong  ("satid")
    name      = info.getString("satname")

    val tleLines = jsonObject.getString("tle").split("\n")
    tleLine1  = tleLines[0].dropLast(1) // remove "\r" at the end of the string
    tleLine2  = tleLines[1]
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

  fun build() : Satellite {
    return Satellite(this)
  }
}