package fhnw.ws6c.sevensat.data.celestrak

class CategoryCall(category: String) : CelesTrakCall() {
  private val category = category
  override fun getTargetUrl(): String = "${celesTrakBaseUrl}?GROUP=$category&FORMAT=json"
}
