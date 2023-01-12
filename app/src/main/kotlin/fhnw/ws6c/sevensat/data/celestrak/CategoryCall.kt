package fhnw.ws6c.sevensat.data.celestrak

import org.json.JSONObject

class CategoryCall(category: String) : CelesTrakCall<JSONObject>() {
  private val category = category
  override fun getTargetUrl(): String = "${celesTrakBaseUrl}?GROUP=$category&FORMAT=json"
}
