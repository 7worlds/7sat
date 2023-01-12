package fhnw.ws6c.sevensat.data.celestrak
import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject

const val celesTrakBaseUrl = "https://celestrak.org/NORAD/elements/gp.php"


abstract class CelesTrakCall<T>: ApiCallable<T> {

  var jsonResponse: T?       = null
  var exception: Exception?  = null

  override fun setResponse(response: T) {
    jsonResponse = response
  }

  override fun getResponse(): T? {
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