package fhnw.ws6c.sevensat.util.extensions

import fhnw.ws6c.sevensat.model.orbitaldata.DEG2RAD
import fhnw.ws6c.sevensat.model.orbitaldata.RAD2DEG


fun Double.toDegrees(): Double = this * RAD2DEG
fun Double.toRadians(): Double = this * DEG2RAD
