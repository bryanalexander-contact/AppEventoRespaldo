package com.example.eventoapp.ui.screens

import android.app.DatePickerDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.io.OutputStream
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearEventoScreen(
    viewModel: EventoViewModel,
    onBack: () -> Unit,
    usuarioId: Int = 1,
    creadorNombre: String = "Usuario Actual"
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(System.currentTimeMillis()) }
    var fechaTexto by remember { mutableStateOf("Seleccionar fecha") }

    // Aquí guardaremos un **string limpio** file://… para Room
    var imagePath by remember { mutableStateOf<String?>(null) }

    // 📸 Tomar foto y guardarla en Pictures/EventLive como archivo REAL
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val filename = "evento_${System.currentTimeMillis()}.jpg"

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EventLive")
            }

            val uri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )

            uri?.let { realUri ->
                val output: OutputStream? = context.contentResolver.openOutputStream(realUri)
                output?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                }

                // Convertimos la URI content:// a file:// para Coil ✔
                val filePathQuery = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(realUri, filePathQuery, null, null, null)

                cursor?.use {
                    if (it.moveToFirst()) {
                        val rawPath = it.getString(0)
                        imagePath = "file://$rawPath" // 🔥 ESTA ES LA CLAVE
                    }
                }
            }
        }
    }

    // 📅 Date Picker
    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            fecha = calendar.timeInMillis
            fechaTexto = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo evento") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // 📸 Botón de foto
            Button(onClick = { cameraLauncher.launch(null) }) {
                Text("📸 Tomar foto")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mostrar la foto guardada
            imagePath?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del evento") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección o lugar") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = duracion,
                onValueChange = { duracion = it },
                label = { Text("Duración (en horas)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { datePicker.show() }) {
                Text("📅 $fechaTexto")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✔ Botón guardar
            Button(onClick = {

                val duracionHoras = duracion.toIntOrNull() ?: 0

                if (
                    nombre.isBlank() ||
                    descripcion.isBlank() ||
                    direccion.isBlank() ||
                    imagePath == null
                ) return@Button

                val evento = EventoEntity(
                    usuarioId = usuarioId,
                    nombre = nombre,
                    descripcion = descripcion,
                    direccion = direccion,
                    fecha = fecha,
                    duracionHoras = duracionHoras,
                    imagenUri = imagePath,   // ✔ Perfecto para Coil
                    creadorNombre = creadorNombre,
                    isGuardado = true
                )

                viewModel.crearEvento(evento)
                onBack()
            }) {
                Text("💾 Guardar evento")
            }
        }
    }
}
