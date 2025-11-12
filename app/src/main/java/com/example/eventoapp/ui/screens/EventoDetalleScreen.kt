package com.example.eventoapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetalleScreen(
    eventoId: Int,
    viewModel: EventoViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var comentario by remember { mutableStateOf(TextFieldValue("")) }
    val comentarios = remember { mutableStateListOf<String>() }

    // Obtener evento desde ViewModel
    val evento by remember { derivedStateOf { viewModel.eventos.value.firstOrNull { it.id == eventoId } } }

    if (evento == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Cargando evento...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(evento!!.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                evento!!.imagenUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(File(uri)),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        viewModel.guardarEventoLocal(context, evento!!)
                        Toast.makeText(context, "Imagen descargada en EventLive ✅", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("📥 Descargar foto")
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Text("👤 Organizador: ${evento!!.creadorNombre}")
                Text("📍 Dirección: ${evento!!.direccion}")
                Text("🕒 Fecha: ${viewModel.formatearFecha(evento!!.fecha)}")
                Text("⏱ Duración: ${evento!!.duracionHoras} horas")
                Spacer(Modifier.height(8.dp))
                Text(evento!!.descripcion)
                Spacer(Modifier.height(16.dp))

                Text("💬 Comentarios", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                comentarios.forEach { c -> Text("- $c") }

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = comentario,
                    onValueChange = { comentario = it },
                    label = { Text("Agregar comentario") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardActions = KeyboardActions(onDone = {
                        if (comentario.text.isNotBlank()) {
                            comentarios.add(comentario.text)
                            comentario = TextFieldValue("")
                        }
                    })
                )

                Spacer(Modifier.height(8.dp))
                Button(onClick = {
                    if (comentario.text.isNotBlank()) {
                        comentarios.add(comentario.text)
                        comentario = TextFieldValue("")
                    }
                }) { Text("Enviar comentario") }
            }
        }
    )
}
