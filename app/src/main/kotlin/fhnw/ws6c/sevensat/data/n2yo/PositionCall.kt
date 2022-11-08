package fhnw.ws6c.sevensat.data.n2yo

class PositionCall(
  satelliteId : Number,
  timeInSec   : Number,
  longitude   : Number,
  latitude    : Number,
  altitude    : Number = 0
): N2yoCall() {

  private val identifier  = "positions"
  private val id          = satelliteId
  private val s           = timeInSec
  private val lng         = longitude
  private val lat         = latitude
  private val alt         = altitude

  override fun getTargetUrl(): String {
    return "$n2yoBaseURL/$identifier/$id/$lat/$lng/$alt/$s$n2yoApiKey"
  }

}