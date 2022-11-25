package fhnw.ws6c.sevensat.util.linalg

import com.mapbox.geojson.Point
import org.junit.Test

class LinalgTest {

  @Test
  fun angleBetweenTwoVectorsTest() {
    // given
    val p1 = Point.fromLngLat(3.0, -2.0)
    val p2 = Point.fromLngLat(1.0, 7.0)

    // when
    val angle = Linalg.angleBetweenPoints(p1,p2)

    //then
    assert(angle < -102.5 && angle > -102.6)
  }
}