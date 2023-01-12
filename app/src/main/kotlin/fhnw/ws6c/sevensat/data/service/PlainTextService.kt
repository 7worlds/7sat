package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.util.tle.TLEParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection

class PlainTextService : RemoteService<Map<Long, Triple<String, String, String>>>() {

  override fun collectingStrategy(connection: HttpURLConnection): Map<Long, Triple<String, String, String>> {

    val reader = BufferedReader(InputStreamReader(connection.inputStream))

    return TLEParser().getMapLineByLine(reader)
  }
}