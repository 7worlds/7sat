package fhnw.ws6c.sevensat.data.satnogs

import fhnw.ws6c.sevensat.data.service.dummy.DummySatnogsService
import fhnw.ws6c.sevensat.model.satellite.SatelliteBuilder
import junit.framework.Assert.assertEquals
import org.json.JSONObject
import org.junit.Test

class DetailCallTest {

  @Test
  fun testGetResponse() {
    // given
    val service = DummySatnogsService()
    val call    = DetailCall(25544)

    // when
    service.loadRemoteData(call)
    val result = call.getResponse()

    //then
    val detailObject = result!!.getJSONArray("values")[0] as JSONObject
    val name  = detailObject.getString("name");
    val names = detailObject.getString("names");
    val image = detailObject.getString("image");
    assertEquals(name, "ISS")
    assertEquals(names, "ZARYA")
    assertEquals(image, "https://db-satnogs.freetls.fastly.net/media/satellites/ISS.jpg")
  }
}