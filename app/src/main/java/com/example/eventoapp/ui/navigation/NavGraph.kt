package com.example.eventoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eventoapp.ui.screens.*
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CrearEvento : Screen("crear_evento")
    object Registro : Screen("registro")
    object Eventos : Screen("eventos")
    object EventoDetalle : Screen("evento_detalle/{eventoId}") // id del evento como argumento
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
                    navController.navigate(Screen.Eventos.route)
                },
                onIrRegistro = {
                    navController.navigate(Screen.Registro.route)
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Registro.route) {
            RegistroUsuarioScreen(
                usuarioViewModel = usuarioViewModel,
                onRegistroExitoso = {
                    navController.navigate(Screen.Eventos.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onIrLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Crear Evento
        composable(Screen.CrearEvento.route) {
            CrearEventoScreen(
                viewModel = eventoViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de lista de eventos (EventoScreen)
        composable(Screen.Eventos.route) {
            EventoScreen(
                viewModel = eventoViewModel,
                navController = navController
            )
        }

        // Pantalla de detalle del evento
        composable(
            route = Screen.EventoDetalle.route,
            arguments = listOf(navArgument("eventoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventoId = backStackEntry.arguments?.getInt("eventoId") ?: 0
            val evento = eventoViewModel.eventos.value.firstOrNull { it.id == eventoId }

            evento?.let {
                EventoDetalleScreen(
                    evento = it,
                    viewModel = eventoViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
