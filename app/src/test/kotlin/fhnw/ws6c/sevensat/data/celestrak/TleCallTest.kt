package fhnw.ws6c.sevensat.data.celestrak

import fhnw.ws6c.sevensat.data.service.SatelliteService
import org.junit.Assert.*

import org.junit.Test

class TleCallTest {

  @Test
  fun getStringResponse() {
    val call = TleCall()
    val service = SatelliteService<String>()
    service.loadRemoteData(call)
    println(call.getResponse())
  }
}