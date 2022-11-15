package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.PositionCall
import fhnw.ws6c.sevensat.data.n2yo.TleCall
import org.json.JSONObject
import java.net.HttpURLConnection

class DefaultN2yoService : Service<JSONObject> {

  override fun loadRemoteData(call: ApiCallable<JSONObject>) {

    var data = ""
    when (call) {
      is TleCall      -> data = tleDefaultData()
      is PositionCall -> data = positionDefaultData()
    }
    call.setResponse(JSONObject(data))
  }

  fun tleDefaultData(): String {
    return """
      {
        "info": {
          "satid": 25544,
          "satname": "SPACE STATION",
          "transactionscount": 5
        },
        "tle": "1 25544U 98067A   22312.10734271  .00013585  00000-0  24681-3 0  9991\r\n2 25544  51.6458 343.6266 0006659  48.7066  47.2159 15.49845562367506"
      }
    """.trimIndent()
  }

  fun positionDefaultData(): String {
    return """
      {
        "info": {
          "satname": "SPACE STATION",
          "satid": 25544,
          "transactionscount": 6
        },
        "positions": [
          {
            "satlatitude": 46.43583923,
            "satlongitude": -50.00177714,
            "sataltitude": 419.97,
            "azimuth": 66.94,
            "elevation": 1.07,
            "ra": 185.56103283,
            "dec": 17.99182297,
            "timestamp": 1667891847,
            "eclipsed": true
          }
        ]
      }
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }


}