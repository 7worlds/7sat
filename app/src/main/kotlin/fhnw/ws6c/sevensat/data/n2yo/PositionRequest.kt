package fhnw.ws6c.sevensat.data.n2yo

import org.json.JSONObject

class PositionRequest(
  satelliteId : Number,
  timeInSec   : Number,
  longitude   : Number,
  latitude    : Number,
  altitude    : Number = 0
) : N2yoRequest() {

  private val identifier  = "positions"
  private val id          = satelliteId
  private val s           = timeInSec
  private val lng         = longitude
  private val lat         = latitude
  private val alt         = altitude

  override fun getTargetUrl(): String {
    return "$baseURL/$identifier/$id/$lat/$lng/$alt/$s$apiKey"
  }

  override fun convertTo(jsonObject: JSONObject): JSONObject {
    // TODO: convert to a satellite object
    return jsonObject
  }
}