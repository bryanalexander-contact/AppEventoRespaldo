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
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.ui.viewmodel.EventoViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

// -----------------------------
// Tarjeta de evento (reutilizable)
// -----------------------------
@Composable
fun EventoCard(
    evento: EventoEntity,
    onClick: (EventoEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(evento) },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(evento.nombre, style = MaterialTheme.typography.titleLarge)
            if (evento.creadorNombre.isNotBlank())
                Text("👤 Organizador: ${evento.creadorNombre}", style = MaterialTheme.typography.labelLarge)

            Spacer(Modifier.height(8.dp))

            evento.imagenUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(File(it)),
                    contentDescription = evento.nombre,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            Text("📍 ${evento.direccion}")
            Text("🕒 ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(evento.fecha))}")
            Text("⏱ Duración: ${evento.duracionHoras} horas")
            Spacer(Modifier.height(4.dp))
            Text(evento.descripcion)
        }
    }
}

// -----------------------------
// Pantalla de eventos
// -----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoScreen(
    viewModel: EventoViewModel,
    navController: NavController // NavController para navegar al detalle
) {
    val eventos by viewModel.eventos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("EventLive 🎉") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
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
                EventoCard(evento = evento) {
                    // Navegar a detalle usando NavController
                    navController.currentBackStackEntry?.savedStateHandle?.set("evento", it)
                    navController.navigate("evento_detalle")
                }
            }
        }
    }
}
