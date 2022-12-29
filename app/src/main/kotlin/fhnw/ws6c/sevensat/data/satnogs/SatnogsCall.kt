package fhnw.ws6c.sevensat.data.satnogs
import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject

const val satnogsBaseUrl = "https://db.satnogs.org/api"


abstract class SatnogsCall: ApiCallable<JSONObject> {

  var jsonResponse: JSONObject?   = null
  var exception:    Exception?    = null

  override fun setResponse(response: JSONObject) {
    jsonResponse = response
  }

  override fun getResponse(): JSONObject? {
    return jsonResponse
  }

  override fun hasError(): Boolean {
    return null != exception
  }

  override fun getError(): Exception? {
    return exception
  }

  override fun setError(e: Exception) {
    exception = e
  }
}