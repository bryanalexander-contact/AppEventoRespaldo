package com.example.eventoapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.io.File
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

            items(eventos) { evento ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Navegar al detalle pasando el id del evento
                            navController.navigate("evento_detalle/${evento.id}")
                        },
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            text = evento.nombre,
                            style = MaterialTheme.typography.titleLarge
                        )

                        if (evento.creadorNombre.isNotBlank()) {
                            Text(
                                text = "👤 Organizador: ${evento.creadorNombre}",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        if (evento.imagenUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(File(evento.imagenUri)),
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

fun formatearFecha(timestamp: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(timestamp))
}
