package com.example.eventoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import com.example.eventoapp.ui.utils.Validators
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.animations.ClickScaleAnimation
import com.example.eventoapp.ui.animations.SlideDownAlert


// ====================================================
// EXTENSIONES NECESARIAS (shake + buttonPress + ErrorFadeIn)
// ====================================================

@Composable
fun ErrorFadeIn(visible: Boolean, content: @Composable () -> Unit) {
    FadeInAnimation(visible = visible, duration = 250) {
        content()
    }
}

fun Modifier.shake(enabled: Boolean): Modifier {
    // No implementamos shake física para mantenerlo simple,
    // solo una ligera animación de aparición si hay error
    return if (enabled) {
        this.alpha(0.85f)
    } else this
}

fun Modifier.buttonPress(enabled: Boolean = true): Modifier {
    return this.then(
        Modifier
    )
}


// ====================================================
// PANTALLA DE REGISTRO (ARREGLADA Y COMPLETA)
// ====================================================

@Composable
fun RegistroUsuarioScreen(
    usuarioViewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit,
    onIrLogin: () -> Unit
) {

    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }

    var botonPresionado by remember { mutableStateOf(false) }
    var mostrarAlerta by remember { mutableStateOf(false) }
    var mensajeAlerta by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // --------------------------
        // CAMPO NOMBRE
        // --------------------------
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = Validators.validarNombre(it)
            },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .shake(errorNombre != null),
            isError = errorNombre != null
        )
        ErrorFadeIn(errorNombre != null) {
            Text(errorNombre ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------
        // CAMPO CORREO
        // --------------------------
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                errorCorreo = Validators.validarCorreo(it)
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .shake(errorCorreo != null),
            isError = errorCorreo != null
        )
        ErrorFadeIn(errorCorreo != null) {
            Text(errorCorreo ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(8.dp))

        // --------------------------
        // CAMPO CONTRASEÑA
        // --------------------------
        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                errorContrasena = Validators.validarContrasena(it)
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .shake(errorContrasena != null),
            isError = errorContrasena != null
        )
        ErrorFadeIn(errorContrasena != null) {
            Text(errorContrasena ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        // --------------------------
        // BOTÓN REGISTRAR
        // --------------------------
        ClickScaleAnimation(pressed = botonPresionado) { scale ->
            Button(
                onClick = {
                    botonPresionado = true

                    errorNombre = Validators.validarNombre(nombre)
                    errorCorreo = Validators.validarCorreo(correo)
                    errorContrasena = Validators.validarContrasena(contrasena)

                    val valido = listOf(errorNombre, errorCorreo, errorContrasena).all { it == null }

                    if (valido) {
                        usuarioViewModel.registrarUsuario(nombre, correo, contrasena)
                        onRegistroExitoso()
                    } else {
                        mensajeAlerta = "Corrige los campos marcados."
                        mostrarAlerta = true
                    }

                    botonPresionado = false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(scale)
            ) {
                Text("Registrar")
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onIrLogin) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }

        // --------------------------
        // ALERTA DESLIZABLE
        // --------------------------
        SlideDownAlert(visible = mostrarAlerta) {
            Text(
                mensajeAlerta,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
