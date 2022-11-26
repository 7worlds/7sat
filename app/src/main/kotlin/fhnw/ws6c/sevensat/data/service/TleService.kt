package fhnw.ws6c.sevensat.data.service

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class TleService : RemoteService<Map<Long, Triple<String, String, String>>>() {

  override fun collectingStrategy(connection: HttpURLConnection): Map<Long, Triple<String, String, String>> {
    val result = mutableMapOf<Long, Triple<String,String, String>>()

    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    val it = reader.lineSequence().iterator()

    while(it.hasNext()) {
      var snd = ""
      var thd = ""
      val fst = it.next()
      if(it.hasNext()) snd = it.next()
      if(it.hasNext()) thd = it.next()
      val t = Triple(fst, snd, thd)
      val id = t.third.split(" ")[1].toLong()
      result[id] = t
    }
    return result
  }
}