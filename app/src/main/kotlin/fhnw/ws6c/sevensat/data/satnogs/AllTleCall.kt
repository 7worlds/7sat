package fhnw.ws6c.sevensat.data.satnogs


class AllTleCall() : SatnogsCall() {

  private val identifier = "tle"

  override fun getTargetUrl(): String {
    return "$satnogsBaseUrl/$identifier/"
  }
}