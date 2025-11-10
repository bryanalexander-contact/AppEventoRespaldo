package com.example.eventoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eventoapp.ui.screens.*
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel
import com.example.eventoapp.data.Model.entities.EventoEntity

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object CrearEvento : Screen("crear_evento")
    object Registro : Screen("registro")
    object Eventos : Screen("eventos") // pantalla de lista de eventos
    object EventoDetalle : Screen("evento_detalle") // pantalla de detalle
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
                    navController.navigate(Screen.Eventos.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
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

        // Pantalla Home (opcional, si quieres mantenerla como inicio alternativo)
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

        // -------------------------------
        // Pantalla de eventos con tarjetas
        // -------------------------------
        composable(Screen.Eventos.route) {
            EventoScreen(
                viewModel = eventoViewModel,
                navController = navController
            )
        }

        // -------------------------------
        // Pantalla de detalle del evento
        // -------------------------------
        composable(Screen.EventoDetalle.route) {
            val evento = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<EventoEntity>("evento")

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
