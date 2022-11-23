package fhnw.ws6c.sevensat.util.linalg

import com.mapbox.geojson.Point
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

object Linalg {

  fun calculateAngle(point1: Point, point2: Point): Double {
    val vec1X = point2.longitude()  - point1.longitude()
    val vec1Y = point2.latitude()   - point1.latitude()
    val vec2X = 1.0
    val vec2Y = 0.0
    val inBetween = (vec1X * vec2X + vec1Y * vec2Y) / sqrt(vec1X.pow(2.0) + vec1Y.pow(2.0)) * sqrt(vec2X.pow(2.0) + vec2Y.pow(2.0))
    return acos(inBetween).toDegrees()
  }
}