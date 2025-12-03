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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.ui.animations.CardAppearAnimation
import com.example.eventoapp.ui.animations.FadeInAnimation
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import com.example.eventoapp.network.WeatherResponse
import com.example.eventoapp.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    // Estado para clima
    var temperatura by remember { mutableStateOf<Float?>(null) }
    var climaError by remember { mutableStateOf<String?>(null) }

    // Llamada a la API del clima (sólo demostración)
    LaunchedEffect(Unit) {
        ApiClient.weatherApi.getWeather("Santiago", "TU_API_KEY").enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    temperatura = response.body()?.main?.temp
                } else {
                    climaError = "Error al obtener clima"
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                climaError = "Falló la llamada: ${t.message}"
            }
        })
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
            Row(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Columna izquierda con clima
                Column(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        "Clima 🌤",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    when {
                        temperatura != null -> Text("${temperatura}°C", style = MaterialTheme.typography.bodyLarge)
                        climaError != null -> Text(climaError ?: "", color = MaterialTheme.colorScheme.error)
                        else -> Text("Cargando...", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Columna derecha con eventos
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
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
                }
            }
        }
    }
}

fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(timestamp))
}
