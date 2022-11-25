package fhnw.ws6c.sevensat

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.gps.UserLocationSubject
import fhnw.ws6c.sevensat.data.service.ApiService
import fhnw.ws6c.sevensat.data.service.TleService
import fhnw.ws6c.sevensat.model.MapModel
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI


object SevenSatApp : EmobaApp {
  private lateinit var model: SevenSatModel
  private val jsonService   = ApiService()
  private val stringService = TleService()


  override fun initialize(activity: ComponentActivity) {
    model = SevenSatModel(jsonService, stringService)
  }

  @Composable
  override fun CreateUI(activity: ComponentActivity) {
    val mapModel = MapModel(activity)
    SevenSatUI(model, mapModel, activity)
  }
}
