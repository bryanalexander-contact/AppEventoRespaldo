package com.example.eventoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.eventoapp.ui.navigation.AppNavGraph
import com.example.eventoapp.ui.theme.EventoAppTheme
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EventoAppTheme {
                val navController = rememberNavController()

                // ViewModels SIN factories (consumen API directamente)
                val eventoViewModel: EventoViewModel = viewModel()
                val usuarioViewModel: UsuarioViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    eventoViewModel = eventoViewModel,
                    usuarioViewModel = usuarioViewModel
                )
            }
        }
    }
}
