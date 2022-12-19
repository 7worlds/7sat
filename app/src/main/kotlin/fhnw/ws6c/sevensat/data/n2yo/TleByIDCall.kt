package fhnw.ws6c.sevensat.data.n2yo

class TleByIDCall(satelliteId: Number) : N2yoCall() {

  private val identifier  = "tle"
  private val id          = satelliteId

  override fun getTargetUrl(): String {
    return "$n2yoBaseURL/$identifier/$id$n2yoApiKey"
  }
}