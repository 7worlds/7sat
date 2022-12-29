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
    val allNames = listOf(
      "ATHENOXAT 1",
      "0 ISS (ZARYA)",
      "NEMO-HD",
      "0 UPMSAT-2"
    )

    val allIds = listOf(
      41168L,
      25544L,
      46277L,
      46276L,
    )

    // when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    val tleData = result!!.getJSONArray("values") as JSONArray
    for (i in 0 until tleData.length()){
      val obj = tleData[i] as JSONObject
      assertEquals(obj.getString("tle0"), allNames[i])
      assertEquals(obj.getLong("norad_cat_id"), allIds[i])
    }
  }
}