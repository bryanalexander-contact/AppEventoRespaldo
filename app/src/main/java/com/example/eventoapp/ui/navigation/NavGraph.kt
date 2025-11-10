package com.example.eventoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventoapp.ui.screens.CrearEventoScreen
import com.example.eventoapp.ui.screens.HomeScreen
import com.example.eventoapp.ui.screens.LoginScreen
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CrearEvento : Screen("crear_evento")
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    eventoViewModel: EventoViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                usuarioViewModel = usuarioViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onIrRegistro = {
                    // Aquí luego puedes navegar al registro
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = eventoViewModel,
                onCrearEvento = { navController.navigate(Screen.CrearEvento.route) }
            )
        }

        composable(Screen.CrearEvento.route) {
            CrearEventoScreen(
                viewModel = eventoViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
