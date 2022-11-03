package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.ApiCallable

abstract class N2yoRequest: ApiCallable {

  val apiKey = "&apiKey=7VQNXG-LKUJR6-FW9HLQ-4XGS"
  val baseURL = "https://api.n2yo.com/rest/v1/satellite"

  var exception: Exception? = null

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