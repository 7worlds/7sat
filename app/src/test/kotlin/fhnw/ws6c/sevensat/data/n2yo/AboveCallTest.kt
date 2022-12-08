package fhnw.ws6c.sevensat.data.n2yo

import fhnw.ws6c.sevensat.data.service.dummy.DummyN2yoService
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import junit.framework.Assert.assertEquals
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import java.util.stream.IntStream
import java.util.stream.Stream

class AboveCallTest : N2yoCallTest(){

  private val lng   = -76.014
  private val lat   = 41.702
  private val radius = 30
  private val altitude = 0

  override fun newApiCall(): N2yoCall {
    return AboveCall(lng, lat, altitude, radius, SatelliteCategory.ALL)
  }

  @Test
  fun resultTest() {
    //given
    val call    = AboveCall(lng, lat, altitude, radius, SatelliteCategory.ALL)
    val service = DummyN2yoService()

    //when
    service.loadRemoteData(call)

    //then
    val json  = call.getResponse()
    val allSatellites :JSONArray = json!!.getJSONArray("above")

    val result = (0 until allSatellites.length())
      .map { allSatellites.get(it) as JSONObject }
      .map { it.getInt("satid") }
      .toList()

    assertEquals(result, listOf(4708, 10441))
    assertEquals(false, call.hasError())
  }
}