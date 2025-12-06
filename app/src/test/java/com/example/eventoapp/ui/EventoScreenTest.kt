package com.example.eventoapp.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.eventoapp.network.EventoResponse
import com.example.eventoapp.ui.screens.EventoScreen
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class EventoScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun muestra_evento_en_pantalla() {
        val fakeViewModel = object: EventoViewModel() {
            override val eventos = MutableStateFlow(
                listOf(EventoResponse(1,1,"Test Evento","Desc","Dir",1690000000L,2,null,"Creador",false))
            )
        }

        composeRule.setContent {
            EventoScreen(
                viewModel = fakeViewModel,
                navController = androidx.navigation.compose.rememberNavController(),
                onCrearEvento = {}
            )
        }

        composeRule.onNodeWithText("Test Evento").assertIsDisplayed()
    }
}
