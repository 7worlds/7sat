package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.service.SatelliteService
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

abstract class N2yoCallTest {

  private lateinit var call:  N2yoCall

  abstract fun newApiCall() : N2yoCall

  private fun setup(){
    call = newApiCall();
  }

  @Test
  fun loadFromUrlTest() {
    //given
    setup()
    val service = SatelliteService<JSONObject>()

    //when
    service.loadRemoteData(call)

    //then
    assertEquals(false, call.hasError())
  }
}