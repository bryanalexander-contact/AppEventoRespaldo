package com.example.eventoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.eventoapp.data.local.database.AppDatabase
import com.example.eventoapp.data.local.repository.EventoRepository
import com.example.eventoapp.ui.screens.CrearEventoScreen
import com.example.eventoapp.ui.screens.HomeScreen
import com.example.eventoapp.ui.screens.LoginScreen
import com.example.eventoapp.ui.theme.EventoAppTheme
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Room y el repositorio
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "eventos_db"
        ).build()
        val repository = EventoRepository(db.eventoDao())

        // Factory para inyectar el repositorio en el ViewModel
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventoViewModel(repository) as T
            }
        }

        setContent {
            EventoAppTheme {
                var currentScreen by remember { mutableStateOf("login") }
                val viewModel: EventoViewModel = viewModel(factory = factory)

                when (currentScreen) {
                    "login" -> LoginScreen(onLoginSuccess = { currentScreen = "home" })
                    "home" -> HomeScreen(viewModel = viewModel)
                    "crear" -> CrearEventoScreen(viewModel = viewModel)
                }
            }
        }
    }
}
