package fhnw.ws6c.sevensat.data.satnogs

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.JsonServiceTest
import fhnw.ws6c.sevensat.data.service.dummy.DummySatnogsService
import junit.framework.Assert.assertEquals
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

class AllTleCallTest : JsonServiceTest() {

  override fun newApiCall(): ApiCallable<JSONObject> {
    return AllTleCall()
  }

  @Test
  fun testGetResponse() {
    // given
    val service = DummySatnogsService()
    val call    = AllTleCall()

    // when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    val tleData = result!!.getJSONArray("values") as JSONArray
//    assertEquals(name, "ISS")
  }
}