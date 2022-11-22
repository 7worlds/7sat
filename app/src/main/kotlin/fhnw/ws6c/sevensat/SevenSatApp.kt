package fhnw.ws6c.sevensat

import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fhnw.ws6c.EmobaApp
import fhnw.ws6c.sevensat.data.service.ApiService
import fhnw.ws6c.sevensat.data.service.TleService
import fhnw.ws6c.sevensat.model.Screen
import fhnw.ws6c.sevensat.model.SevenSatModel
import fhnw.ws6c.sevensat.ui.SevenSatUI
import fhnw.ws6c.sevensat.ui.components.LoadingUI
import fhnw.ws6c.sevensat.ui.theme.SevenSatTheme


object SevenSatApp : EmobaApp {
    private lateinit var model: SevenSatModel
    private val jsonService = ApiService()
    private val stringService = TleService()


    override fun initialize(activity: ComponentActivity) {
        model = SevenSatModel(jsonService, stringService)
    }

    @Composable
    override fun CreateUI(activity: ComponentActivity) {
        SevenSatTheme {
            Crossfade(targetState = model.activescreen) { screen ->
                when (screen) {
                    Screen.HOME -> SevenSatUI(model = model, activity)
                    Screen.LOADING -> LoadingUI(model = model)

                }
            }
        }
    }




}
