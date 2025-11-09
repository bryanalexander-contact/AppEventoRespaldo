package com.example.eventoapp.ui.screens

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onIrRegistro: () -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val usuarioActual by usuarioViewModel.usuarioActual.observeAsState()

    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            onLoginSuccess()
        } else if (errorMsg != null) {
            // nada, ya mostrado
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("EventLive 🎉", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (correo.isNotBlank() && contrasena.isNotBlank()) {
                    usuarioViewModel.login(correo, contrasena)
                } else {
                    errorMsg = "Completa todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onIrRegistro) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }

        errorMsg?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
