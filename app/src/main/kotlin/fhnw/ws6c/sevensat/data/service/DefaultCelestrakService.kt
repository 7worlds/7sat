package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.PositionCall
import fhnw.ws6c.sevensat.data.n2yo.TleCall
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.util.Collections

class DefaultCelestrakService : Service<Map<Long, Triple<String, String, String>>> {

  override fun loadRemoteData(call: ApiCallable<Map<Long, Triple<String, String, String>>>) {

    val data = """
      CALSPHERE 1
      1 00900U 64063C   22317.76945716  .00000677  00000+0  71091-3 0  9998
      2 00900  90.1843  43.6866 0024921 292.2280 165.8439 13.73967756891167
      CALSPHERE 2
      1 00902U 64063E   22317.81117932  .00000047  00000+0  56457-4 0  9998
      2 00902  90.1993  46.8708 0020302  76.2103  52.4335 13.52727325678968
      LCS 1
      1 01361U 65034C   22317.56591239  .00000002  00000+0 -87720-3 0  9993
      2 01361  32.1443  51.2440 0011664  45.4888 314.6561  9.89302012 79995
    """.trimIndent()

    val result = mutableMapOf<Long, Triple<String,String, String>>()

    val reader = BufferedReader(InputStreamReader(data.byteInputStream()))
    val it = reader.lineSequence().iterator()

    while(it.hasNext()) {
      var fst = ""
      var snd = ""
      var thd = ""
      if(it.hasNext()) fst = it.next()
      if(it.hasNext()) snd = it.next()
      if(it.hasNext()) thd = it.next()

      val t = Triple(fst, snd, thd)
      val id = t.third.split(" ")[1].toLong()
      result[id] = t
    }
    call.setResponse(result)
  }

  override fun collectingStrategy(connection: HttpURLConnection): Map<Long, Triple<String, String, String>> {
    TODO("Not yet implemented")
  }
}