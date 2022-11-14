package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.celestrak.TleCall
import fhnw.ws6c.sevensat.data.n2yo.n2yoApiKey
import fhnw.ws6c.sevensat.data.n2yo.n2yoBaseURL
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.json.JSONException
import org.json.JSONObject
import org.junit.Test
import java.io.IOException

class SatelliteServiceTest {

  private fun createRequestObject(
    urlTarget: () -> String,
    responseAction: (JSONObject) -> Unit,
  ) : ApiCallable<JSONObject> {
    val request = object :ApiCallable<JSONObject> {
      var exception: Exception? = null
      var jsonResponse: JSONObject? = null

      override fun getTargetUrl(): String {
        val target = urlTarget()
        return "$n2yoBaseURL$target$n2yoApiKey"
      }

      override fun getResponse(): JSONObject? {
        return jsonResponse
      }

      override fun setResponse(response: JSONObject) {
        jsonResponse = response
        responseAction(jsonResponse!!)
      }

      override fun hasError(): Boolean { return null != exception }

      override fun getError(): Exception? { return exception }

      override fun setError(e: Exception) { exception = e }
    }
    return request
  }

  @Test
  fun simpleRequestTest() {

    //given
    val service = ApiService()
    val call = createRequestObject ({ "/tle/25544/" }, {} )

    //when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    assertEquals(false, call.hasError())
    assertTrue(result?.length()!! > 0)
  }

  @Test
  fun callThrowsJSONExceptionTest() {

    //given
    val service = ApiService()
    val call = createRequestObject({ "/tobias/bräm/" }, {})

    //when
    service.loadRemoteData(call)

    //then
    assertTrue(call.hasError())
    assertTrue(call.getError() is JSONException)
  }

  @Test
  fun callThrowsIOExceptionTest() {

    //given
    val errorMessage = "Something went wrong"
    val service = ApiService()
    val call = createRequestObject({ "/tle/25544/" }, { throw IOException(errorMessage) })

    //when
    service.loadRemoteData(call)

    //then
    assertTrue(call.hasError())
    assertTrue(call.getError() is IOException)
    assertEquals(call.getError()?.message, errorMessage)
  }

  @Test
  fun tleServiceTest() {
    //given
    val service = TleService()
    val tleCall = TleCall()

    //when
    service.loadRemoteData(tleCall)

    //then
    assertEquals(tleCall.getResponse()!![25544]!!.first.trim(), "ISS (ZARYA)".trim())
  }
}