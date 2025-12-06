package com.example.eventoapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.ui.animations.CardAppearAnimation
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.network.DummyClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(
    viewModel: EventoViewModel,
    navController: NavController,
    onCrearEvento: () -> Unit
) {
    val eventos by viewModel.eventos.collectAsState(initial = emptyList())

    // ----- Estado para la "quote" (DummyJSON) -----
    var quoteText by remember { mutableStateOf<String?>(null) }
    var quoteLoading by remember { mutableStateOf(false) }
    var quoteError by remember { mutableStateOf<String?>(null) }

    // Exception handler para coroutines en Compose
    val handler = remember {
        CoroutineExceptionHandler { _, throwable ->
            Log.e("EventoScreen", "Coroutine error", throwable)
        }
    }

    // Función para cargar una quote (la llamamos al entrar y desde botón)
    val scope = rememberCoroutineScope()
    fun loadQuote() {
        scope.launch(handler) {
            quoteLoading = true
            quoteError = null
            try {
                val q = DummyClient.dummyApi.getRandomQuote()
                quoteText = "\"${q.quote}\" — ${q.author}"
            } catch (e: Exception) {
                Log.e("EventoScreen", "Error fetching quote", e)
                quoteError = "No se pudo obtener frase: ${e.message ?: "error"}"
                quoteText = null
            } finally {
                quoteLoading = false
            }
        }
    }

    // Cargar al iniciar la pantalla
    LaunchedEffect(Unit) {
        loadQuote()
    }

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

                                evento.imagenUri?.let { uri ->
                                    val fixedUri = uri
                                    Image(
                                        painter = rememberAsyncImagePainter(fixedUri),
                                        contentDescription = evento.nombre,
                                        modifier = Modifier
                                            .height(200.dp)
                                            .fillMaxWidth()
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }

                                Text("📍 ${evento.direccion}")
                                Text("🕒 ${viewModel.formatearFecha(evento.fecha)}")
                                Text("⏱ Duración: ${evento.duracionHoras} horas")
                                Spacer(Modifier.height(4.dp))
                                Text(evento.descripcion)
                            }
                        }
                    }
                }

                // ITEM: Frase Dummy (al final)
                item {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        "Frase aleatoria 💬",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Spacer(Modifier.height(8.dp))

                    when {
                        quoteLoading -> {
                            Text("Cargando frase...", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))
                        }
                        quoteText != null -> {
                            Text(quoteText ?: "", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(8.dp))
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.padding(8.dp)) {
                                Button(onClick = { loadQuote() }) {
                                    Text("Otra frase")
                                }
                            }
                        }
                        quoteError != null -> {
                            Text(quoteError ?: "Error", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.padding(8.dp)) {
                                Button(onClick = { loadQuote() }) {
                                    Text("Reintentar")
                                }
                            }
                        }
                        else -> {
                            // Caso por defecto
                            Text("Sin frase disponible", modifier = Modifier.padding(8.dp))
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.padding(8.dp)) {
                                Button(onClick = { loadQuote() }) {
                                    Text("Cargar frase")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(48.dp))
                }
            }
        }
    }
}
