package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.service.dummy.DummyN2yoService
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import junit.framework.Assert.assertEquals
import org.junit.Test

class TleCallTest : JsonServiceTest(){

  private val idISS = 25544L

  override fun newApiCall(): N2yoCall {
    return TleByIDCall(idISS)
  }

  @Test
  fun resultTest() {
    //given
    val call = TleByIDCall(idISS)
    val service = DummyN2yoService()

    //when
    service.loadRemoteData(call)

    //then
    val json = call.getResponse()
    val sat = SatelliteBuilder().withN2yoTleJsonData(json!!)

    assertEquals(sat.noradId, idISS)
    assertEquals(sat.tleLine1, "1 25544U 98067A   22312.10734271  .00013585  00000-0  24681-3 0  9991")
    assertEquals(sat.tleLine2, "2 25544  51.6458 343.6266 0006659  48.7066  47.2159 15.49845562367506")
  }
}