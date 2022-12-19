package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.service.JsonService
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

abstract class JsonServiceTest {

  private lateinit var call:  ApiCallable<JSONObject>

  abstract fun newApiCall() : ApiCallable<JSONObject>

  private fun setup(){
    call = newApiCall();
  }

  @Test
  fun loadFromUrlTest() {
    //given
    setup()
    val service = JsonService()

    //when
    service.loadRemoteData(call)

    //then
    assertEquals(false, call.hasError())
  }
}