package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.ApiCallable

const val celestrakBaseURL = "https://celestrak.org/NORAD/elements/gp.php"


class TleCall: ApiCallable<Map<Long, Triple<String, String, String>>> {
  var mapResponse: Map<Long, Triple<String, String, String>>?   = null
  var exception:    Exception?    = null

  override fun getTargetUrl(): String = "$celestrakBaseURL?GROUP=active&FORMAT=tle"

  override fun getResponse(): Map<Long, Triple<String, String, String>>? {
    return mapResponse
  }

  override fun setResponse(response: Map<Long, Triple<String, String, String>>) {
    mapResponse = response
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