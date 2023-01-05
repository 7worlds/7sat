package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.JsonServiceTest
import fhnw.ws6c.sevensat.data.service.dummy.DummyCelesTrakService
import fhnw.ws6c.sevensat.data.service.dummy.DummySatnogsService
import junit.framework.Assert.assertEquals
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

class CelesTrakTest : JsonServiceTest() {

  override fun newApiCall(): ApiCallable<JSONObject> {
    return CategoryCall("stations")
  }

  @Test
  fun testGetResponse() {
    // given
    val service = DummyCelesTrakService()
    val call    = CategoryCall("stations")


    val allIds = listOf(
      44713,
      44714
    )

    // when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    val data = result!!.getJSONArray("values") as JSONArray
    for (i in 0 until data.length()){
      val obj = data[i] as JSONObject
      assertEquals(obj.getInt("NORAD_CAT_ID"), allIds[i])
    }
  }
}