package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.satnogs.AllTleCall
import fhnw.ws6c.sevensat.data.satnogs.DetailByIdCall
import fhnw.ws6c.sevensat.data.service.Service
import org.json.JSONObject
import java.net.HttpURLConnection

class DummySatnogsService : Service<JSONObject> {

  override fun loadRemoteData(call: ApiCallable<JSONObject>) {

    var data = ""
    when (call) {
      is DetailByIdCall -> data = defaultDetailData()
      is AllTleCall     -> data = defaultTleData()
    }
    call.setResponse(JSONObject(data))
  }

  private fun defaultDetailData(): String {
    return """
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
  }

  private fun defaultTleData(): String {
    return """
        {
        values: [
              {
                  "tle0": "ATHENOXAT 1",
                  "tle1": "1 41168U 15077C   22362.57374461  .00025590  00000+0  87352-3 0  9990",
                  "tle2": "2 41168  14.9842  85.3273 0008171 105.6157  23.5785 15.28021359389961",
                  "tle_source": "Celestrak (SatNOGS)",
                  "sat_id": "VTHG-6292-4004-9767-5017",
                  "norad_cat_id": 41168,
                  "updated": "2022-12-29T03:23:46.176236+0000"
              },
              {
                  "tle0": "0 ISS (ZARYA)",
                  "tle1": "1 25544U 98067A   22363.25793862  .00019597  00000-0  35381-3 0  9992",
                  "tle2": "2 25544  51.6443  90.2587 0005264 202.6699   8.6468 15.49722318375433",
                  "tle_source": "Space-Track.org",
                  "sat_id": "XSKZ-5603-1870-9019-3066",
                  "norad_cat_id": 25544,
                  "updated": "2022-12-29T07:23:42.455634+0000"
              },
              {
                  "tle0": "NEMO-HD",
                  "tle1": "1 46277U 20061F   22362.78671429  .00003505  00000+0  17230-3 0  9992",
                  "tle2": "2 46277  97.3703  68.6903 0001206 142.3144 314.7987 15.18673449128342",
                  "tle_source": "Celestrak (active)",
                  "sat_id": "RQZT-2978-8493-5026-5902",
                  "norad_cat_id": 46277,
                  "updated": "2022-12-29T03:23:54.363697+0000"
              },
              {
                  "tle0": "0 UPMSAT-2",
                  "tle1": "1 46276U 20061E   22361.92131729  .00006792  00000-0  31892-3 0  9992",
                  "tle2": "2 46276  97.3708  68.2957 0001365 143.6472 216.4856 15.19941932128243",
                  "tle_source": "Space-Track.org",
                  "sat_id": "UCSJ-1439-6113-9699-8457",
                  "norad_cat_id": 46276,
                  "updated": "2022-12-28T19:23:56.811270+0000"
              }
          ]
      }
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }
}