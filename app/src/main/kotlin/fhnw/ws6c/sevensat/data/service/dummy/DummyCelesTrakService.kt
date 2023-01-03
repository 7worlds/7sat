package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.celestrak.CategoryCall
import fhnw.ws6c.sevensat.data.service.Service
import org.json.JSONObject
import java.net.HttpURLConnection

class DummyCelesTrakService : Service<JSONObject> {

  override fun loadRemoteData(call: ApiCallable<JSONObject>) {

    var data = ""
    when (call) {
      is CategoryCall -> data = defaultStarlinkData()
    }
    call.setResponse(JSONObject(data))
  }


  private fun defaultStarlinkData(): String {
    return """
        {
        values: [
        {
        "OBJECT_NAME":"STARLINK-1007",
        "OBJECT_ID":"2019-074A",
        "EPOCH":"2023-01-03T06:10:18.647328",
        "MEAN_MOTION":15.06397347,
        "ECCENTRICITY":0.0002153,
        "INCLINATION":53.0541,
        "RA_OF_ASC_NODE":0.5063,
        "ARG_OF_PERICENTER":39.4434,
        "MEAN_ANOMALY":320.6712,
        "EPHEMERIS_TYPE":0,
        "CLASSIFICATION_TYPE":"U",
        "NORAD_CAT_ID":44713,
        "ELEMENT_SET_NO":999,
        "REV_AT_EPOCH":17386,
        "BSTAR":4.5226e-5,
        "MEAN_MOTION_DOT":3.92e-6,
        "MEAN_MOTION_DDOT":0
        },
        {
        "OBJECT_NAME":"STARLINK-1008",
        "OBJECT_ID":"2019-074B",
        "EPOCH":"2023-01-03T00:53:12.462720",
        "MEAN_MOTION":15.0638664,
        "ECCENTRICITY":0.0001756,
        "INCLINATION":53.0565,
        "RA_OF_ASC_NODE":1.4827,
        "ARG_OF_PERICENTER":60.7932,
        "MEAN_ANOMALY":299.3232,
        "EPHEMERIS_TYPE":0,
        "CLASSIFICATION_TYPE":"U",
        "NORAD_CAT_ID":44714,
        "ELEMENT_SET_NO":999,
        "REV_AT_EPOCH":17383,
        "BSTAR":0.00014553,
        "MEAN_MOTION_DOT":1.886e-5,
        "MEAN_MOTION_DDOT":0
        }
        ]
        }
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }
}