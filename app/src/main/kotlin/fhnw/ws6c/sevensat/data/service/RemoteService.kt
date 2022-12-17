package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

abstract class RemoteService<T> : Service<T> {

  override fun loadRemoteData (call: ApiCallable<T>) {

    try {
      val url = URL(call.getTargetUrl())
      val connection = url.openConnection() as HttpURLConnection
      connection.setRequestProperty("Accept", "application/json")
      connection.connect()
      try {
        val answer = collectingStrategy(connection)
        call.setResponse(answer)
      } catch (jsonError: JSONException) {
        call.setError(jsonError)
      }
    } catch (ioError: IOException) {
      call.setError(ioError)
    }
  }
}