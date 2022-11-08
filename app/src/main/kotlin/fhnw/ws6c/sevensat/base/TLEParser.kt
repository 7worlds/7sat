package fhnw.ws6c.sevensat.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.InputStream
import kotlin.math.pow

class TLEParser(private val defaultDispatcher: CoroutineDispatcher) {
  suspend fun parseTLEStream(tleStream: InputStream): List<OrbitalData> =
    withContext(defaultDispatcher) {
      val tleStrings = mutableListOf(String(), String(), String())
      val parsedItems = mutableListOf<OrbitalData>()
      var lineIndex = 0
      tleStream.bufferedReader().forEachLine { line ->
        tleStrings[lineIndex] = line
        if (lineIndex < 2) {
          lineIndex++
        } else {
          val isLineOneValid = tleStrings[1].substring(0, 1) == "1"
          val isLineTwoValid = tleStrings[2].substring(0, 1) == "2"
          if (!isLineOneValid && !isLineTwoValid) return@forEachLine
          parseTLE(tleStrings)?.let { tle -> parsedItems.add(tle) }
          lineIndex = 0
        }
      }
      return@withContext parsedItems
    }

  private fun parseTLE(tle: List<String>): OrbitalData? {
    if (tle[1].substring(0, 1) != "1" && tle[2].substring(0, 1) != "2") {
      return null
    }
    try {
      val name: String = tle[0].trim()
      val epoch: Double = tle[1].substring(18, 32).toDouble()
      val meanmo: Double = tle[2].substring(52, 63).toDouble()
      val eccn: Double = tle[2].substring(26, 33).toDouble() / 10000000.0
      val incl: Double = tle[2].substring(8, 16).toDouble()
      val raan: Double = tle[2].substring(17, 25).toDouble()
      val argper: Double = tle[2].substring(34, 42).toDouble()
      val meanan: Double = tle[2].substring(43, 51).toDouble()
      val catnum: Int = tle[1].substring(2, 7).trim().toInt()
      val bstar: Double = 1.0e-5 * tle[1].substring(53, 59).toDouble() /
          10.0.pow(tle[1].substring(60, 61).toDouble())
      return OrbitalData(name, epoch, meanmo, eccn, incl, raan, argper, meanan, catnum, bstar)
    } catch (exception: Exception) {
      return null
    }
  }
}