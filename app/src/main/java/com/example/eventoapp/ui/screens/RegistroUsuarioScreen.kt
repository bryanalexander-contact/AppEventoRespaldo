package com.example.eventoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

@Composable
fun RegistroUsuarioScreen(usuarioViewModel: UsuarioViewModel) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Normal") }

    Column(
        Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo") })
        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(10.dp))
        Button(
            onClick = { usuarioViewModel.registrarUsuario(nombre, correo, contrasena) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar")
        }

        Spacer(Modifier.height(10.dp))

    }
}
