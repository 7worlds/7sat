package fhnw.ws6c.sevensat.model.satellite

import fhnw.ws6c.sevensat.model.orbitaldata.GeoPos
import fhnw.ws6c.sevensat.model.orbitaldata.OrbitalData
import fhnw.ws6c.sevensat.model.orbitaldata.SatPos

data class Satellite(
  val noradId: Number,
  val name: String,
  var description: String,
  var coordinates: Map<Long, Triple<Double, Double, Double>>, // lat, lng, alt
  var tleLine1: String,
  var tleLine2: String,
  var orbitalData: OrbitalData
) {
  constructor(builder: SatelliteBuilder) : this(
    builder.noradId,
    builder.name,
    builder.description,
    builder.coordinates,
    builder.tleLine1,
    builder.tleLine2,
    builder.orbitalData!!
  )

  fun getPosition(timeStamp: Long): SatPos {
    return orbitalData.getPosition(GeoPos(0.0, 0.0), timeStamp)
  }
}
