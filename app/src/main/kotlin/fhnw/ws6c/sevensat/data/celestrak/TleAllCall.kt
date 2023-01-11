package fhnw.ws6c.sevensat.data.celestrak

const val celestrakBaseURL = "https://celestrak.org/NORAD/elements/gp.php"

class TleAllCall: CelesTrakCall<Map<Long, Triple<String, String, String>>>() {
  override fun getTargetUrl(): String = "$celestrakBaseURL?GROUP=active&FORMAT=tle"
}