package com.example.eventoapp.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.data.local.entities.EventoEntity
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun CrearEventoScreen(viewModel: EventoViewModel) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Abre la cámara para tomar una foto
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            val filename = "evento_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, filename)
            FileOutputStream(file).use { out ->
                it.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            imageUri = Uri.fromFile(file)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = { launcher.launch(null) }) {
            Text("📸 Tomar foto")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar imagen tomada
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campos de texto básicos
        BasicTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth()
        )

        BasicTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            modifier = Modifier.fillMaxWidth()
        )

        BasicTextField(
            value = direccion,
            onValueChange = { direccion = it },
            modifier = Modifier.fillMaxWidth()
        )

        BasicTextField(
            value = duracion,
            onValueChange = { duracion = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            // Fecha actual como timestamp (Long)
            val fechaActual = System.currentTimeMillis()
            val duracionHoras = duracion.toIntOrNull() ?: 0

            val evento = EventoEntity(
                usuarioId = 0, // temporal hasta que tengas login
                nombre = nombre,
                descripcion = descripcion,
                direccion = direccion,
                fecha = fechaActual,
                duracionHoras = duracionHoras,
                imagenUri = imageUri?.path
            )

            viewModel.crearEvento(evento)
        }) {
            Text("Guardar evento")
        }
    }
}
