package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SatelliteService<T> : Service<T> {

  override fun loadRemoteData (call: ApiCallable<T>) {

    try {
      val url = URL(call.getTargetUrl())
      val connection = url.openConnection() as HttpURLConnection
      connection.connect()

      try {
        val reader = BufferedReader(InputStreamReader(connection.inputStream))
        val jsonString = reader.readText()
        call.setResponse(jsonString)
        reader.close()
      } catch (jsonError: JSONException) {
        call.setError(jsonError)
      }

    } catch (ioError: IOException) {
      call.setError(ioError)
    }
  }
}