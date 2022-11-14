package fhnw.ws6c.sevensat.data


interface ApiCallable<T> {
  fun getTargetUrl()            : String
  fun getResponse()             : T?
  fun setResponse(response: String)
  fun hasError()                : Boolean
  fun getError()                : Exception?
  fun setError(e: Exception)
}