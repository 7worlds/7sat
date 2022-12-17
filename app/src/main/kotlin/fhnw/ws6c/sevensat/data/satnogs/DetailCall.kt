package fhnw.ws6c.sevensat.data.satnogs

import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject


const val satnogsBaseUrl = "https://db.satnogs.org/api"

class DetailCall(satelliteId: Number) : ApiCallable<JSONObject> {
  var resp:      JSONObject?   = null
  var exception: Exception?    = null
  private val id               = satelliteId


  override fun getTargetUrl(): String = "${satnogsBaseUrl}/satellites/?norad_cat_id=$id"

  override fun getResponse(): JSONObject? {
    return resp
  }

  override fun setResponse(response: JSONObject) {
    resp = response
  }

  override fun hasError(): Boolean {
    return exception != null
  }

  override fun getError(): Exception? {
    return exception
  }

  override fun setError(e: Exception) {
    exception = e
  }
}
