package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.celestrak.TleAllCall
import fhnw.ws6c.sevensat.data.service.Service
import fhnw.ws6c.sevensat.util.tle.TLEParser
import java.io.BufferedReader
import java.net.HttpURLConnection

class DummyCelesTrakPlainTextService : Service<Map<Long, Triple<String, String, String>>> {

  override fun loadRemoteData(call: ApiCallable<Map<Long, Triple<String, String, String>>>) {

    var data = ""
    when (call) {
      // perpared for future calls
      is TleAllCall -> data = defaultAllTleData()
    }
    val reader = BufferedReader(data.reader())
    call.setResponse(TLEParser().getMapLineByLine(reader))
  }

  private fun defaultAllTleData(): String {
    return """
        CALSPHERE 1
        1 00900U 64063C   23009.87912071  .00000685  00000+0  71762-3 0  9991
        2 00900  90.1805  44.7768 0028883 119.9470  53.6490 13.74037150899004
        CALSPHERE 2
        1 00902U 64063E   23009.37320921  .00000041  00000+0  48450-4 0  9990
        2 00902  90.1943  47.9950 0016414 270.0460 101.6446 13.52732371686611
        LCS 1
        1 01361U 65034C   23009.92172607  .00000024  00000+0  19706-2 0  9997
        2 01361  32.1407 275.0802 0010996 250.4704 109.4605  9.89302455 85677
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): Map<Long, Triple<String, String, String>> {
    TODO("Not yet implemented")
  }
}