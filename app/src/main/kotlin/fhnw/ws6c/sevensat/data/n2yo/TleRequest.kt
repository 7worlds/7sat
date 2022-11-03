package fhnw.ws6c.sevensat.data.n2yo

import org.json.JSONObject

class TleRequest(satelliteId: Number) : N2yoRequest() {

  private val identifier = "tle"
  private val id = satelliteId

  override fun getTargetUrl(): String {
    return "$baseURL/$identifier/$id$apiKey"
  }

  override fun convertTo(jsonObject: JSONObject): JSONObject {
    // TODO: convert to a satellite object
    return jsonObject
  }
}