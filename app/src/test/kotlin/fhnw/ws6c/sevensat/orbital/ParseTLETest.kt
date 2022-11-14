package fhnw.ws6c.sevensat.orbital

import fhnw.ws6c.sevensat.util.tle.TLEParser
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import fhnw.ws6c.sevensat.model.orbitaldata.GeoPos
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 *  ACHTUNG: Auf Android arbeiten wir (noch) mit JUNIT 4
 */

class ParseTLETest {

  private val dataParser = TLEParser()

  private val name = "ISS (ZARYA)"
  private val line1 = "1 25544U 98067A   22312.10734271  .00013585  00000-0  24681-3 0  9991"
  private val line2 = "2 25544  51.6458 343.6266 0006659  48.7066  47.2159 15.49845562367506"


  @Test
  fun `Given valid TLE stream returns valid data`() {
    val orbitalData = dataParser.parseSingleTLE(name, line1, line2)!!
    assert(orbitalData.epoch == 22312.10734271)

    val satPos = orbitalData.getPosition(
      GeoPos(8.220250, 47.478519),
      1667895364
    )

    assert(satPos.latitude.toDegrees() == -40.25788239876213)
    assert(satPos.longitude.toDegrees() == 147.84377862894618)
  }
}