package com.example.eventoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.eventoapp.data.Model.database.AppDatabase
import com.example.eventoapp.data.Model.repository.EventoRepository
import com.example.eventoapp.ui.navigation.AppNavGraph
import com.example.eventoapp.ui.theme.EventoAppTheme
import com.example.eventoapp.ui.viewmodel.EventoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la BD Room y el repositorio
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "eventos_db"
        ).build()
        val repository = EventoRepository(db.eventoDao())

        // Factory para inyectar el repositorio al ViewModel
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventoViewModel(repository) as T
            }
        }

        setContent {
            EventoAppTheme {
                val navController = rememberNavController()
                val viewModel: EventoViewModel = viewModel(factory = factory)

                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
