package fhnw.ws6c.sevensat.data.satnogs

class DetailByIdCall(satelliteId: Number) : SatnogsCall() {
  private val id = satelliteId
  override fun getTargetUrl(): String = "${satnogsBaseUrl}/satellites/?norad_cat_id=$id"
}
