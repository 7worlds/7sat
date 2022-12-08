package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.n2yo.AboveCall
import fhnw.ws6c.sevensat.data.n2yo.PositionByIdCall
import fhnw.ws6c.sevensat.data.n2yo.TleByIDCall
import fhnw.ws6c.sevensat.data.service.Service
import org.json.JSONObject
import java.net.HttpURLConnection

class DummyN2yoService : Service<JSONObject> {

  override fun loadRemoteData(call: ApiCallable<JSONObject>) {

    var data = ""
    when (call) {
      is TleByIDCall      -> data = tleDefaultData()
      is PositionByIdCall -> data = positionDefaultData()
      is AboveCall        -> data = aboveDefaultData()
    }
    call.setResponse(JSONObject(data))
  }

  private fun tleDefaultData(): String {
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

  private fun positionDefaultData(): String {
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

  private fun aboveDefaultData(): String {
    return """
            {
              "info": {
                "category": "ANY",
                "transactionscount": 2,
                "satcount": 2
              },
              "above": [
                {
                  "satid": 4708,
                  "satname": "THORAD AGENA D DEB",
                  "intDesignator": "1970-025BW",
                  "launchDate": "1970-04-08",
                  "satlat": 40.9775,
                  "satlng": -77.1527,
                  "satalt": 962.5594
                },
                {
                  "satid": 10441,
                  "satname": "COSMOS 839 DEB",
                  "intDesignator": "1976-067S",
                  "launchDate": "1976-07-08",
                  "satlat": 40.4484,
                  "satlng": -74.1042,
                  "satalt": 1910.1397
                },
              ]
            }
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }


}