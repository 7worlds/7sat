package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.JsonServiceTest
import fhnw.ws6c.sevensat.data.service.dummy.DummyCelesTrakJsonService
import fhnw.ws6c.sevensat.data.service.dummy.DummyCelesTrakPlainTextService
import junit.framework.Assert.assertEquals
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test

class CelesTrakTest : JsonServiceTest() {

  override fun newApiCall(): ApiCallable<JSONObject> {
    return CategoryCall("stations")
  }

  @Test
  fun testLoadCategoryData() {
    // given
    val service = DummyCelesTrakJsonService()
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

  @Test
  fun getStringResponse() {
    //given
    val call = TleAllCall()
    val service = DummyCelesTrakPlainTextService()

    //when
    service.loadRemoteData(call)

    //then
    val result = call.getResponse()
    val testTripe1 = Triple(
      "CALSPHERE 1",
      "1 00900U 64063C   23009.87912071  .00000685  00000+0  71762-3 0  9991",
      "2 00900  90.1805  44.7768 0028883 119.9470  53.6490 13.74037150899004"
    )
    val testTripe2 = Triple(
      "CALSPHERE 2",
      "1 00902U 64063E   23009.37320921  .00000041  00000+0  48450-4 0  9990",
      "2 00902  90.1943  47.9950 0016414 270.0460 101.6446 13.52732371686611"
    )
    val testTripe3 = Triple(
      "LCS 1",
      "1 01361U 65034C   23009.92172607  .00000024  00000+0  19706-2 0  9997",
      "2 01361  32.1407 275.0802 0010996 250.4704 109.4605  9.89302455 85677"
    )
    assertEquals(result!![900], testTripe1)
    assertEquals(result[902],   testTripe2)
    assertEquals(result[1361],  testTripe3)
  }
}