package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable
import java.net.HttpURLConnection

interface Service<T> {
  fun loadRemoteData(call: ApiCallable<T>)
  fun collectingStrategy(connection :HttpURLConnection): T
}