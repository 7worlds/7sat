package fhnw.ws6c.sevensat.model.satellite

data class Satellite(
  val noradId:      Number,
  val name:         String,
  var description:  String,
  var coordinates:  Map<Long, Triple<Double, Double, Double>>, // lat, lng, alt
  var tleLine1:     String,
  var tleLine2:     String,
  )
{
  constructor(builder: SatelliteBuilder) : this(
    builder.noradId,
    builder.name,
    builder.description,
    builder.coordinates,
    builder.tleLine1,
    builder.tleLine2
  )
}
