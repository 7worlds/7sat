package fhnw.ws6c.sevensat.data.n2yo

class AboveCall(
  longitude   : Number,
  latitude    : Number,
  altitude    : Number = 0,
  radius      : Number,
  category    : SatelliteCategory,
): N2yoCall() {
  private val identifier  = "above"
  private val lng         = longitude
  private val lat         = latitude
  private val alt         = altitude
  private val r           = radius
  private val cat         = category

  override fun getTargetUrl(): String {
    return "$n2yoBaseURL/$identifier/$lat/$lng/$alt/$r/${cat.id}/$n2yoApiKey"
  }
}