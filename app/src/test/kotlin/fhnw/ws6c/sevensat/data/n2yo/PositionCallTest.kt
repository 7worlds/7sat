package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.service.dummy.DummyN2yoService
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import junit.framework.Assert.assertEquals
import org.junit.Test

class PositionCallTest : N2yoCallTest() {

  private val issId = 25544L
  private val lng   = -76.014
  private val lat   = 41.702

  override fun newApiCall(): N2yoCall {
    return PositionCall(issId, 1, lng, lat)
  }

  @Test
  fun resultTest() {
    //given
    val call = PositionCall(issId, 1, lng, lat)
    val service = DummyN2yoService()

    //when
    service.loadRemoteData(call)

    //then
    val json = call.getResponse()
    val sat = SatelliteBuilder().withPositionData(json!!)

    assertEquals(sat.noradId, issId)
    assertEquals(sat.name, "SPACE STATION")
    assertEquals(sat.coordinates[issId]?.first,   46.43583923 )
    assertEquals(sat.coordinates[issId]?.second,  -50.00177714 )
    assertEquals(sat.coordinates[issId]?.third,   419.97 )
  }
}