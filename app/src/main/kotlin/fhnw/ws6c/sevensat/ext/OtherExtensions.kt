package fhnw.ws6c.sevensat.ext

import fhnw.ws6c.sevensat.model.DEG2RAD
import fhnw.ws6c.sevensat.model.RAD2DEG


fun Double.toDegrees(): Double = this * RAD2DEG
fun Double.toRadians(): Double = this * DEG2RAD
