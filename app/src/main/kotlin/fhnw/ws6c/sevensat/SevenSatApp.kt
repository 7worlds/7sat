package fhnw.ws6c.sevensat

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI


object SevenSatApp : EmobaApp {
    private lateinit var model: SevenSatApp

    override fun initialize(activity: ComponentActivity) {
        model = SevenSatApp
    }

    @Composable
    override fun CreateUI(activity: ComponentActivity) {
        SevenSatUI(model, activity)
    }
}
