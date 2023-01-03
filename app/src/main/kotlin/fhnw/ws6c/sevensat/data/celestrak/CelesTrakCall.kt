package fhnw.ws6c.sevensat.data.celestrak
import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject

const val celesTrakBaseUrl = "https://celestrak.org/NORAD/elements/gp.php"


abstract class CelesTrakCall: ApiCallable<JSONObject> {

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