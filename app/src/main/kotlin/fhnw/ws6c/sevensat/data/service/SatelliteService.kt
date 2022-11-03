package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SatelliteService {

  val noradId = 25544
  val lng = -76.014
  val lat = 41.702
  val alt = 0
  val seconds = 2

  fun loadRemoteData (request: ApiCallable): JSONObject {

    // positions
//    val requestURL = "$baseURL/$resource/$noradId/$lat/$lng/$alt/${seconds}${apiKey}"


    return try {
      val url = URL(request.getTargetUrl())
      val connection = url.openConnection() as HttpURLConnection
      connection.connect()

      val reader = BufferedReader(InputStreamReader(connection.inputStream))
      val jsonString = reader.readText()

      val resultObject = JSONObject(jsonString)
      reader.close()
      request.convertTo(resultObject)
    } catch (e: Exception) {
      request.setError(e)
      JSONObject()
    }
  }
}