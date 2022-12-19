package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.service.dummy.DummyCelestrakService
import junit.framework.Assert.assertEquals
import org.junit.Test

class TleCallTest {

  @Test
  fun getStringResponse() {
    //given
    val call = TleAllCall()
    val service = DummyCelestrakService()

    //when
    service.loadRemoteData(call)

    //then
    val result = call.getResponse()
    val testTripe1 = Triple(
      "CALSPHERE 1",
      "1 00900U 64063C   22317.76945716  .00000677  00000+0  71091-3 0  9998",
      "2 00900  90.1843  43.6866 0024921 292.2280 165.8439 13.73967756891167"
    )
    val testTripe2 = Triple(
      "CALSPHERE 2",
      "1 00902U 64063E   22317.81117932  .00000047  00000+0  56457-4 0  9998",
      "2 00902  90.1993  46.8708 0020302  76.2103  52.4335 13.52727325678968"
    )
    val testTripe3 = Triple(
      "LCS 1",
      "1 01361U 65034C   22317.56591239  .00000002  00000+0 -87720-3 0  9993",
      "2 01361  32.1443  51.2440 0011664  45.4888 314.6561  9.89302012 79995"
    )
    assertEquals(result!![900], testTripe1)
    assertEquals(result[902],   testTripe2)
    assertEquals(result[1361],  testTripe3)
  }
}