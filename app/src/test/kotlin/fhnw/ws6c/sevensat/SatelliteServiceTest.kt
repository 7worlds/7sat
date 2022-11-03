package fhnw.ws6c.sevensat

import android.icu.text.Transliterator.Position
import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.PositionRequest
import fhnw.ws6c.sevensat.data.n2yo.TleRequest
import fhnw.ws6c.sevensat.data.service.SatelliteService
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class SatelliteServiceTest {

  private val idISS = 25544
  val lng = -76.014
  val lat = 41.702
  val alt = 0
  val seconds = 2

  @Test
  fun testLoadTleData() {

    //given
    val service = SatelliteService()

    //when
    val tleRequest: ApiCallable = TleRequest(idISS)
    val result = service.loadRemoteData(tleRequest)

    //then
    assertEquals(false, tleRequest.hasError())
    assertTrue(result.length() > 0)
  }

  @Test
  fun testLoadPositionData() {

    //given
    val service = SatelliteService()

    //when
    val tleRequest: ApiCallable = PositionRequest(idISS, 1, lng, lat)
    val result = service.loadRemoteData(tleRequest)

    //then
    assertEquals(false, tleRequest.hasError())
    assertTrue(result.length() > 0)
  }

}