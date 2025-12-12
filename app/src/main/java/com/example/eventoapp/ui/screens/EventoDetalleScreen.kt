package com.example.eventoapp.ui.screens

import android.content.ContentValues
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
import coil.request.ImageRequest
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

    // Cargar eventos actualizados al entrar
    LaunchedEffect(Unit) {
        viewModel.obtenerEventos()
    }

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

    val baseFallback = "http://98.88.76.248:4001"

    val displayUri = remember(evento.imagenUri) {
        evento.imagenUri?.let { uri ->
            when {
                uri.startsWith("http://") || uri.startsWith("https://") -> uri
                uri.startsWith("file://") -> uri
                uri.startsWith("content://") -> uri
                uri.startsWith("/uploads") -> "$baseFallback$uri"
                uri.startsWith("uploads/") -> "$baseFallback/$uri"
                !uri.contains("://") -> "$baseFallback/uploads/$uri"
                else -> uri
            }
        }
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

                // -------------------------
                //      IMAGEN DEL EVENTO
                // -------------------------
                displayUri?.let { imgUrl ->
                    val req = ImageRequest.Builder(LocalContext.current)
                        .data(imgUrl)
                        .crossfade(true)
                        .build()

                    Image(
                        painter = rememberAsyncImagePainter(req),
                        contentDescription = evento.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    // -------------------------
                    // BOTÓN DESCARGAR FOTO
                    // -------------------------
                    var pressed by remember { mutableStateOf(false) }
                    ClickScaleAnimation(pressed = pressed) { scale ->
                        Button(
                            onClick = {
                                scope.launch {
                                    pressed = true
                                    val ok = saveImageToMediaStore(context, imgUrl)
                                    if (ok) {
                                        Toast.makeText(context, "Imagen descargada en la galería ✔️", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error al guardar imagen", Toast.LENGTH_SHORT).show()
                                    }
                                    delay(150)
                                    pressed = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer { scaleX = scale; scaleY = scale }
                        ) {
                            Text("📥 Descargar foto")
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                }

                // -------------------------
                //      DATOS DEL EVENTO
                // -------------------------
                Text("👤 Organizador: ${evento.creadorNombre ?: "Desconocido"}")
                Spacer(Modifier.height(6.dp))

                Text("📍 Dirección: ${evento.direccion}")
                Spacer(Modifier.height(6.dp))

                Text("🕒 Fecha: ${viewModel.formatearFecha(evento.fecha)}")
                Spacer(Modifier.height(6.dp))

                Text("⏱ Duración: ${evento.duracionHoras} horas")
                Spacer(Modifier.height(10.dp))

                Text("📝 Descripción:", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(evento.descripcion)
            }
        }
    }
}

/**
 * Guarda una imagen que puede estar en:
 * - http(s)://
 * - file://
 * - content://
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
            val newUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?: return@withContext false

            val outStream: OutputStream? = resolver.openOutputStream(newUri)

            outStream?.use { out ->

                when {
                    uriString.startsWith("http") -> {
                        val input: InputStream = URL(uriString).openStream()
                        input.use { it.copyTo(out) }
                    }
                    uriString.startsWith("content://") || uriString.startsWith("file://") -> {
                        val input = resolver.openInputStream(android.net.Uri.parse(uriString))
                        input?.use { it.copyTo(out) }
                    }
                    else -> { // Último fallback
                        val input: InputStream = URL(uriString).openStream()
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
