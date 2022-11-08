package fhnw.ws6c.sevensat.model.satellite

import fhnw.ws6c.sevensat.data.n2yo.PositionCall
import fhnw.ws6c.sevensat.data.n2yo.TleCall
import fhnw.ws6c.sevensat.data.service.DefaultService
import junit.framework.Assert.assertEquals
import org.junit.Test


class SatelliteBuilderTest {

  @Test
  fun dataCombineTest() {

    //given
    val service = DefaultService()
    val tle = TleCall(-1)
    val pos = PositionCall(-1, -1, -1, -1)

    //when
    service.loadRemoteData(tle)
    service.loadRemoteData(pos)

    //then
    val description = "An amazing description"
    val sat = SatelliteBuilder()
      .withTleJsonData(tle.getResponse()!!)
      .withPositionData(pos.getResponse()!!)
      .withDescription(description)
      .build()

    assertEquals(sat.noradId, 25544L)
    assertEquals(sat.name, "SPACE STATION")
    assertEquals(sat.coordinates[sat.noradId]?.first, 46.43583923)
    assertEquals(sat.coordinates[sat.noradId]?.second, -50.00177714)
    assertEquals(sat.coordinates[sat.noradId]?.third, 419.97)
    assertEquals(sat.description, description)
    assertEquals(
      sat.tleLine1,
      "1 25544U 98067A   22312.10734271  .00013585  00000-0  24681-3 0  9991"
    )
    assertEquals(
      sat.tleLine2,
      "2 25544  51.6458 343.6266 0006659  48.7066  47.2159 15.49845562367506"
    )
  }

}