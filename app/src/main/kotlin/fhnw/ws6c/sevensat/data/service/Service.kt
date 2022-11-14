package fhnw.ws6c.sevensat.data.service

import fhnw.ws6c.sevensat.data.ApiCallable

interface Service<T> {
  fun loadRemoteData(call: ApiCallable<T>)
}