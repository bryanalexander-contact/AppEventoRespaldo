package com.example.eventoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventoapp.ui.screens.CrearEventoScreen
import com.example.eventoapp.ui.screens.HomeScreen
import com.example.eventoapp.ui.screens.LoginScreen
import com.example.eventoapp.ui.screens.RegistroUsuarioScreen
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CrearEvento : Screen("crear_evento")
    object Registro : Screen("registro") // nueva ruta
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    eventoViewModel: EventoViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        // Pantalla de Login
        composable(Screen.Login.route) {
            LoginScreen(
                usuarioViewModel = usuarioViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onIrRegistro = {
                    navController.navigate(Screen.Registro.route) // navegar a registro
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Registro.route) {
            RegistroUsuarioScreen(
                usuarioViewModel = usuarioViewModel,
                onRegistroExitoso = {
                    // Una vez registrado, ir a Home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onIrLogin = {
                    // Volver al Login
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = eventoViewModel,
                onCrearEvento = { navController.navigate(Screen.CrearEvento.route) }
            )
        }

        // Pantalla Crear Evento
        composable(Screen.CrearEvento.route) {
            CrearEventoScreen(
                viewModel = eventoViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
