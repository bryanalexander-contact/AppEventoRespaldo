package com.example.eventoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import com.example.eventoapp.ui.animations.*
import com.example.eventoapp.ui.utils.Validators
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onIrRegistro: () -> Unit
) {

    // Campos
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    // Errores locales
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }

    // Datos del ViewModel
    val usuarioActual by usuarioViewModel.usuarioActual.observeAsState()
    val mensajeError by usuarioViewModel.mensajeError.observeAsState()

    // Animación del botón
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Navegar si el login fue exitoso
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) onLoginSuccess()
    }

    // ----------------------------
    // ANIMACIÓN FADE IN GLOBAL
    // ----------------------------
    FadeInAnimation {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "EventLive 🎉",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(16.dp))

            // ----------------------------
            // CAMPO CORREO
            // ----------------------------
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    errorCorreo = Validators.validarCorreo(it)
                },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorCorreo != null
            )

            if (errorCorreo != null) {
                Text(errorCorreo ?: "", color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(8.dp))

            // ----------------------------
            // CAMPO CONTRASEÑA
            // ----------------------------
            OutlinedTextField(
                value = contrasena,
                onValueChange = {
                    contrasena = it
                    errorContrasena = Validators.validarContrasenaLogin(it)
                },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                isError = errorContrasena != null
            )

            if (errorContrasena != null) {
                Text(errorContrasena ?: "", color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            // ----------------------------
            // BOTÓN LOGIN (ANIMADO)
            // ----------------------------
            ClickScaleAnimation(pressed = pressed) { scale ->
                Button(
                    onClick = {
                        pressed = true

                        errorCorreo = Validators.validarCorreo(correo)
                        errorContrasena = Validators.validarContrasenaLogin(contrasena)

                        val valido = errorCorreo == null && errorContrasena == null

                        if (valido) {
                            usuarioViewModel.login(correo, contrasena)
                        }

                        scope.launch {
                            // animation feel
                            kotlinx.coroutines.delay(180)
                            pressed = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(scale)
                ) {
                    Text("Iniciar sesión")
                }
            }

            Spacer(Modifier.height(8.dp))

            // Ir a registro
            TextButton(onClick = onIrRegistro) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }

            // ----------------------------
            // ERROR GLOBAL DEL VIEWMODEL
            // ----------------------------
            if (mensajeError != null) {
                Text(mensajeError ?: "", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}
