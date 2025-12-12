package com.example.eventoapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.eventoapp.ui.animations.CardAppearAnimation
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: EventoViewModel,
    navController: NavController,
    onCrearEvento: () -> Unit
) {
    val eventos by viewModel.eventos.collectAsState()
    val baseFallback = "http://98.88.76.248:4001"
    val ctx = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EventLive 🎉") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    Button(onClick = onCrearEvento) {
                        Text("➕ Crear evento")
                    }
                }
            )
        }
    ) { padding ->
        FadeInAnimation {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                if (eventos.isEmpty()) {
                    item {
                        Text(
                            "No hay eventos aún 😔",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                itemsIndexed(eventos) { index, evento ->
                    CardAppearAnimation(index = index) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    navController.navigate("evento_detalle/${evento.id}")
                                },
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(evento.nombre, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(8.dp))

                                evento.imagenUri?.let { uriRaw ->
                                    val fixedUri = when {
                                        uriRaw.startsWith("http://") || uriRaw.startsWith("https://") -> uriRaw
                                        uriRaw.startsWith("content://") || uriRaw.startsWith("file://") -> uriRaw
                                        uriRaw.startsWith("/uploads") -> "$baseFallback$uriRaw"
                                        uriRaw.startsWith("uploads/") -> "$baseFallback/$uriRaw"
                                        !uriRaw.contains("://") -> "$baseFallback/uploads/$uriRaw"
                                        else -> uriRaw
                                    }
                                    val req = ImageRequest.Builder(ctx)
                                        .data(fixedUri)
                                        .crossfade(true)
                                        .build()
                                    Image(
                                        painter = rememberAsyncImagePainter(req),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(200.dp)
                                            .fillMaxWidth()
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }

                                Text("📍 ${evento.direccion}")
                                Text("🕒 ${formatearFecha(evento.fecha)}")
                                Text("⏱ Duración: ${evento.duracionHoras} horas")
                                Spacer(Modifier.height(4.dp))
                                Text(evento.descripcion)
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(48.dp))
                }
            }
        }
    }
}

fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(timestamp))
}
