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

    // ----------------------------
    // ANIMACIÓN SHAKE PARA INPUTS
    // ----------------------------
    @Composable
    fun Modifier.shake(enabled: Boolean): Modifier {
        val offset by animateFloatAsState(
            targetValue = if (enabled) 18f else 0f,
            animationSpec = keyframes {
                durationMillis = 400
                0f at 0
                -14f at 50
                14f at 100
                -10f at 150
                10f at 200
                -4f at 250
                0f at 300
            },
            label = "shake"
        )

        return this.offset(x = offset.dp)
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .shake(errorCorreo != null),
                isError = errorCorreo != null
            )

            SlideDownAlert(visible = errorCorreo != null) {
                Text(
                    errorCorreo ?: "",
                    color = MaterialTheme.colorScheme.error
                )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .shake(errorContrasena != null),
                isError = errorContrasena != null
            )

            SlideDownAlert(visible = errorContrasena != null) {
                Text(
                    errorContrasena ?: "",
                    color = MaterialTheme.colorScheme.error
                )
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

                        pressed = false
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
            SlideDownAlert(visible = mensajeError != null) {
                Text(
                    mensajeError ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
