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
import com.example.eventoapp.data.Model.repository.UsuarioRepository
import com.example.eventoapp.ui.navigation.AppNavGraph
import com.example.eventoapp.ui.theme.EventoAppTheme
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa la BD Room con fallback para desarrollo
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "eventos_db"
        )
            .fallbackToDestructiveMigration() // Esto borra la DB si detecta cambios de esquema
            .build()

        // Repositorios
        val eventoRepository = EventoRepository(db.eventoDao())
        val usuarioRepository = UsuarioRepository(db.usuarioDao())

        // Factory para EventoViewModel
        val eventoFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return EventoViewModel(eventoRepository) as T
            }
        }

        // Factory para UsuarioViewModel
        val usuarioFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return UsuarioViewModel(usuarioRepository) as T
            }
        }

        setContent {
            EventoAppTheme {
                val navController = rememberNavController()

                // ViewModels con sus respectivos factories
                val eventoViewModel: EventoViewModel = viewModel(factory = eventoFactory)
                val usuarioViewModel: UsuarioViewModel = viewModel(factory = usuarioFactory)

                // NavGraph
                AppNavGraph(
                    navController = navController,
                    eventoViewModel = eventoViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }
    }
}
