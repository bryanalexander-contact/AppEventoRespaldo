package com.example.eventoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.scale
import com.example.eventoapp.ui.utils.Validators
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.animations.ClickScaleAnimation
import com.example.eventoapp.ui.animations.SlideDownAlert

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

    val usuarioActual by usuarioViewModel.usuarioActual.observeAsState(initial = null)
    val mensajeErrorVal by usuarioViewModel.mensajeError.observeAsState(initial = null)

    // Navegar cuando user exista (registro ok)
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            onRegistroExitoso()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = Validators.validarNombre(it)
            },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorNombre != null
        )
        if (errorNombre != null) Text(errorNombre ?: "", color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                errorCorreo = Validators.validarCorreo(it)
            },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            isError = errorCorreo != null
        )
        if (errorCorreo != null) Text(errorCorreo ?: "", color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                errorContrasena = Validators.validarContrasena(it)
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = errorContrasena != null
        )
        if (errorContrasena != null) Text(errorContrasena ?: "", color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(16.dp))

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

        if (mensajeErrorVal != null) {
            SlideDownAlert(visible = true) {
                Text(mensajeErrorVal ?: "", color = MaterialTheme.colorScheme.error)
            }
        }

        if (mostrarAlerta) {
            SlideDownAlert(visible = true) {
                Text(mensajeAlerta, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
            }
        }
    }
}
