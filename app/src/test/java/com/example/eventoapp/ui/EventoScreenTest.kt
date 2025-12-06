package com.example.eventoapp.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.example.eventoapp.network.EventoResponse
import com.example.eventoapp.ui.screens.EventoScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

// Test funcional de pantalla EventoScreen sin tocar la app
class EventoScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    // ViewModel mínimo para test, solo expone StateFlow de eventos
    class EventoViewModelParaTest : com.example.eventoapp.ui.viewmodel.EventoViewModel() {
        private val _fakeEventos = MutableStateFlow(
            listOf(
                EventoResponse(
                    id = 1,
                    usuarioId = 1,
                    nombre = "Test Evento",
                    descripcion = "Desc",
                    direccion = "Dir",
                    fecha = 1690000000L,
                    duracionHoras = 2,
                    imagenUri = null,
                    creadorNombre = "Creador",
                    isGuardado = false
                )
            )
        )

        val fakeEventos: StateFlow<List<EventoResponse>> = _fakeEventos
    }

    @Test
    fun muestra_evento_en_pantalla() {
        val viewModelParaTest = EventoViewModelParaTest()

        composeRule.setContent {
            EventoScreen(
                viewModel = viewModelParaTest,
                navController = rememberNavController(),
                onCrearEvento = {}
            )
        }

        // Verifica que el texto del evento se muestre
        composeRule.onNodeWithText("Test Evento").assertIsDisplayed()
    }
}
