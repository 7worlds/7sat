package fhnw.ws6c.sevensat.util.linalg

import com.mapbox.geojson.Point
import fhnw.ws6c.sevensat.util.extensions.toDegrees
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.sqrt

object Linalg {

  fun angleBetweenPoints(point1: Point, point2: Point): Double {
    val vec1X     = point2.longitude() - point1.longitude()
    val vec1Y     = point2.latitude()  - point1.latitude()
    val vec2X     = 1.0
    val vec2Y     = 0.0
    val inBetween =
      dotProduct(vec1X, vec2X, vec1Y, vec2Y) / vectorNorm(vec1X, vec1Y) * vectorNorm(vec2X, vec2Y)
    val angle     = acos(inBetween).toDegrees()

    // it makes a difference if the satellites flying in south or north direction.
    return if (point2.latitude() < point1.latitude()) angle else -angle
  }

  private fun dotProduct(vec1X: Double, vec2X: Double, vec1Y: Double, vec2Y: Double) =
    (vec1X * vec2X + vec1Y * vec2Y)

  private fun vectorNorm(vec1X: Double, vec1Y: Double) =
    sqrt(vec1X.pow(2.0) + vec1Y.pow(2.0))
}