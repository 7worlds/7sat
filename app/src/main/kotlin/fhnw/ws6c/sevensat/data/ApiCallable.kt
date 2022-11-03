package fhnw.ws6c.sevensat.data

import org.json.JSONObject

interface ApiCallable {
  fun getTargetUrl() : String
  fun convertTo(jsonObject: JSONObject) : JSONObject
  fun hasError() : Boolean
  fun getError() : Exception?
  fun setError(e: Exception)
}