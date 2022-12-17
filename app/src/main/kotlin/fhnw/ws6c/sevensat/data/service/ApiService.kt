package fhnw.ws6c.sevensat.data.service

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class ApiService : RemoteService<JSONObject>() {

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    val result = reader.readText()
    reader.close()
    val jsonToken = JSONTokener(result).nextValue()
    if (jsonToken is JSONArray) {
      return jsonToken.toJSONObject(jsonToken)
    }
      return JSONObject(result)
    }
}
