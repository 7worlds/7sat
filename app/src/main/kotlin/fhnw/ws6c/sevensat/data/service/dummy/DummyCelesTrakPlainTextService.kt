package fhnw.ws6c.sevensat.data.service.dummy

import fhnw.ws6c.sevensat.data.ApiCallable
import fhnw.ws6c.sevensat.data.celestrak.CategoryCall
import fhnw.ws6c.sevensat.data.satnogs.AllTleCall
import fhnw.ws6c.sevensat.data.service.Service
import org.json.JSONObject
import java.net.HttpURLConnection

class DummyCelesTrakPlainTextService<T> : Service<T> {

  override fun loadRemoteData(call: ApiCallable<T>) {

    var data = ""
    when (call) {
      is CategoryCall -> data = defaultStarlinkData()
      is AllTleCall   -> data = defaultAllTleData()
    }
      call.setResponse(JSONObject(data) as T)

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

  private fun defaultAllTleData(): String {
    return """
        CALSPHERE 1             
        1 00900U 64063C   23009.87912071  .00000685  00000+0  71762-3 0  9991
        2 00900  90.1805  44.7768 0028883 119.9470  53.6490 13.74037150899004
        CALSPHERE 2             
        1 00902U 64063E   23009.37320921  .00000041  00000+0  48450-4 0  9990
        2 00902  90.1943  47.9950 0016414 270.0460 101.6446 13.52732371686611
        LCS 1                   
        1 01361U 65034C   23009.92172607  .00000024  00000+0  19706-2 0  9997
        2 01361  32.1407 275.0802 0010996 250.4704 109.4605  9.89302455 85677
        TEMPSAT 1               
        1 01512U 65065E   23010.12854522  .00000037  00000+0  55717-4 0  9994
        2 01512  89.9215 217.0594 0070276 168.7591 316.1982 13.33465696792916
        CALSPHERE 4A            
        1 01520U 65065H   23010.58694510  .00000127  00000+0  22625-3 0  9993
        2 01520  89.9828 129.9089 0069753  40.8242 330.8876 13.35926268795722
        OPS 5712 (P/L 160)      
        1 02826U 67053A   23009.84800230  .00005108  00000+0  12845-2 0  9993
        2 02826  69.9275 164.5068 0003069  24.6994 335.4287 14.51747614882673
        LES-5                   
        1 02866U 67066E   23010.11229948 -.00000103  00000+0  00000+0 0  9992
        2 02866   0.9546 222.3493 0056652  64.2723 297.6503  1.09426041117302
    """.trimIndent()
  }

  override fun collectingStrategy(connection: HttpURLConnection): JSONObject {
    TODO("Not yet implemented")
  }
}