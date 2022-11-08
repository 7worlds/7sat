package fhnw.ws6c.sevensat.model

import kotlin.math.*

data class SatPos(
    var azimuth: Double = 0.0,
    var elevation: Double = 0.0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var altitude: Double = 0.0,
    var distance: Double = 0.0,
    var distanceRate: Double = 0.0,
    var theta: Double = 0.0,
    var time: Long = 0L,
    var phase: Double = 0.0,
    var eclipseDepth: Double = 0.0,
    var eclipsed: Boolean = false,
    var aboveHorizon: Boolean = false
) {

    fun getDownlinkFreq(freq: Long): Long {
        return (freq.toDouble() * (SPEED_OF_LIGHT - distanceRate * 1000.0) / SPEED_OF_LIGHT).toLong()
    }

    fun getUplinkFreq(freq: Long): Long {
        return (freq.toDouble() * (SPEED_OF_LIGHT + distanceRate * 1000.0) / SPEED_OF_LIGHT).toLong()
    }

    fun getOrbitalVelocity(): Double {
        val earthG = 6.674 * 10.0.pow(-11)
        val earthM = 5.98 * 10.0.pow(24)
        val radius = 6.37 * 10.0.pow(6) + altitude * 10.0.pow(3)
        return sqrt(earthG * earthM / radius) / 1000
    }

    fun getRangeCircle(): List<GeoPos> {
        val rangeCirclePoints = mutableListOf<GeoPos>()
        val beta = acos(EARTH_RADIUS / (EARTH_RADIUS + altitude)) // * EARTH_RADIUS = radiusKm
        for (azimuth in 0..720) {
            val rads = azimuth * DEG2RAD
            val lat = asin(sin(latitude) * cos(beta) + (cos(latitude) * sin(beta) * cos(rads)))
            val lon = (longitude + atan2(
                sin(rads) * sin(beta) * cos(latitude),
                cos(beta) - sin(latitude) * sin(lat)
            ))
            rangeCirclePoints.add(GeoPos(lat * RAD2DEG, lon * RAD2DEG))
        }
        return rangeCirclePoints
    }
}
