package fhnw.ws6c.sevensat.data.satnogs

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.JsonServiceTest
import fhnw.ws6c.sevensat.data.service.dummy.DummySatnogsService
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

class DetailCallTest : JsonServiceTest() {

  override fun newApiCall(): ApiCallable<JSONObject> {
    return DetailByIdCall(25544);
  }

  @Test
  fun testGetResponse() {
    // given
    val service = DummySatnogsService()
    val call    = DetailByIdCall(25544)

    // when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    val detailObject = result!!.getJSONArray("values")[0] as JSONObject
    val name  = detailObject.getString("name")
    val names = detailObject.getString("names")
    val image = detailObject.getString("image")
    assertEquals(name, "ISS")
    assertEquals(names, "ZARYA")
    assertEquals(image, "satellites/ISS.jpg")
  }
}