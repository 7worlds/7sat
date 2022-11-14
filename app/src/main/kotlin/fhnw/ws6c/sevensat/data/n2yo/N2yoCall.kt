package fhnw.ws6c.sevensat.data.n2yo
import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONObject

const val n2yoApiKey  = "&apiKey=7VQNXG-LKUJR6-FW9HLQ-4XGS"
const val n2yoBaseURL = "https://api.n2yo.com/rest/v1/satellite"

abstract class N2yoCall: ApiCallable<JSONObject> {

  var jsonResponse: JSONObject?   = null
  var exception:    Exception?    = null

  override fun setResponse(jsonString: JSONObject) {
    jsonResponse = jsonString
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