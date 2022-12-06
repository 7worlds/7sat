package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.service.Service
import org.json.JSONObject
import java.net.HttpURLConnection

class DummySatnogsService : Service<JSONObject> {

  override fun loadRemoteData(call: ApiCallable<JSONObject>) {
    val data = """
        {
        values: [
            {
              "norad_cat_id": 25544,
              "name": "ISS",
              "names": "ZARYA",
              "image": "https://db-satnogs.freetls.fastly.net/media/satellites/ISS.jpg",
              "status": "alive",
              "decayed": null,
              "launched": "1998-11-20T00:00:00Z",
              "deployed": "1998-11-20T00:00:00Z",
              "website": "https://www.nasa.gov/mission_pages/station/main/index.html",
              "operator": "None",
              "countries": "RU,US",
              "telemetries": [
                {
                  "decoder": "iss"
                }
              ]
            }
          ]
        }
    """.trimIndent()
    call.setResponse(JSONObject(data))
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }
}