package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject

const val celestrakBaseURL = "https://celestrak.org/NORAD/elements/gp.php"


class TleCall: ApiCallable<String> {
  var stringResponse: String?   = null
  var exception:    Exception?    = null

  override fun getTargetUrl(): String = "$celestrakBaseURL?GROUP=active&FORMAT=tle"

  override fun getResponse(): String? {
    return stringResponse
  }

  override fun setResponse(response: String) {
    stringResponse = response
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