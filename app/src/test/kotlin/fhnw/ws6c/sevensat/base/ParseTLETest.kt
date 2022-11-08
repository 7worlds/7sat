package fhnw.ws6c.sevensat.base

import fhnw.ws6c.sevensat.ext.toDegrees
import fhnw.ws6c.sevensat.model.GeoPos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.Date

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 *  ACHTUNG: Auf Android arbeiten wir (noch) mit JUNIT 4
 */

class ParseTLETest {
    private val testDispatcher = StandardTestDispatcher()
    private val dataParser = TLEParser(testDispatcher)

    private val validTLEStream = """
        ISS (ZARYA)
        1 25544U 98067A   22312.10734271  .00013585  00000-0  24681-3 0  9991
        2 25544  51.6458 343.6266 0006659  48.7066  47.2159 15.49845562367506
    """.trimIndent().byteInputStream()


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given valid TLE stream returns valid data`() = runTest(testDispatcher) {
        val parsedList = dataParser.parseTLEStream(validTLEStream)
//        assert(parsedList[0].epoch == 21320.51955234)
        val satellite = parsedList.first().getSatellite()

        val satPos = satellite.getPosition(GeoPos(8.220250, 47.478519), Date().time)

        assertTrue(satPos.latitude.toDegrees() > 0)
    }
}