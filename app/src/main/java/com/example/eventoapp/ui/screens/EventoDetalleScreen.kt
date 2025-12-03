package com.example.eventoapp.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.animations.ClickScaleAnimation
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoDetalleScreen(
    eventoId: Int,
    viewModel: EventoViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val eventos by viewModel.eventos.collectAsState()
    val evento = eventos.firstOrNull { it.id == eventoId }

    if (evento == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cargando evento...")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(evento.nombre) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        FadeInAnimation {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                evento.imagenUri?.let { uri ->
                    val displayUri = when {
                        uri.startsWith("http://") || uri.startsWith("https://") -> uri
                        uri.startsWith("file://") -> uri
                        uri.startsWith("content://") -> uri
                        else -> uri
                    }

                    Image(
                        painter = rememberAsyncImagePainter(displayUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    var pressed by remember { mutableStateOf(false) }
                    ClickScaleAnimation(pressed = pressed) { scale ->
                        Button(
                            onClick = {
                                scope.launch {
                                    pressed = true
                                    // Intent: guardar imagen localmente
                                    val ok = saveImageToMediaStore(context, displayUri)
                                    if (ok) {
                                        Toast.makeText(context, "Imagen guardada en la galería ✅", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "No se pudo guardar la imagen", Toast.LENGTH_SHORT).show()
                                    }
                                    delay(140)
                                    pressed = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    scaleX = scale; scaleY = scale
                                }
                        ) {
                            Text("📥 Descargar foto")
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }

                Text("👤 Organizador: ${evento.creadorNombre}")
                Text("📍 Dirección: ${evento.direccion}")
                Text("🕒 Fecha: ${viewModel.formatearFecha(evento.fecha)}")
                Text("⏱ Duración: ${evento.duracionHoras} horas")
                Spacer(Modifier.height(8.dp))
                Text(evento.descripcion)
            }
        }
    }
}

/**
 * Guarda una imagen que puede estar en:
 * - content://...
 * - file://...
 * - http(s)://...
 *
 * Devuelve true si la operación completó correctamente.
 */
suspend fun saveImageToMediaStore(context: android.content.Context, uriString: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val filename = "evento_${System.currentTimeMillis()}.jpg"
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EventLive")
            }

            val resolver = context.contentResolver
            val newUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues) ?: return@withContext false
            val outStream: OutputStream? = resolver.openOutputStream(newUri)

            outStream?.use { out ->
                when {
                    uriString.startsWith("http://") || uriString.startsWith("https://") -> {
                        // Descargar desde la URL
                        val url = URL(uriString)
                        val input: InputStream = url.openStream()
                        input.use { it.copyTo(out) }
                    }
                    uriString.startsWith("content://") || uriString.startsWith("file://") -> {
                        val input: InputStream? = resolver.openInputStream(android.net.Uri.parse(uriString))
                        input?.use { it.copyTo(out) }
                    }
                    else -> {
                        // intentar tratar como URL
                        val url = URL(uriString)
                        val input: InputStream = url.openStream()
                        input.use { it.copyTo(out) }
                    }
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
