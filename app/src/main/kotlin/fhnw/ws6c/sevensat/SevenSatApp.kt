package fhnw.ws6c.sevensat

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.service.SatelliteService
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI
import org.json.JSONObject


object SevenSatApp : EmobaApp {
    private lateinit var model: SevenSatModel
    val jsonService = SatelliteService<JSONObject>()
    val stringService = SatelliteService<String>()


    override fun initialize(activity: ComponentActivity) {
        model = SevenSatModel(jsonService, stringService)
    }

    @Composable
    override fun CreateUI(activity: ComponentActivity) {
        SevenSatUI(model, activity)
    }
}
