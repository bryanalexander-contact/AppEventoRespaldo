package com.example.eventoapp.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.data.Model.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class EventoViewModel(private val repo: EventoRepository) : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoEntity>>(emptyList())
    val eventos: StateFlow<List<EventoEntity>> = _eventos

    init {
        cargarEventos()
    }

    // 🟢 Cargar eventos
    fun cargarEventos() {
        viewModelScope.launch {
            repo.listarEventos().collectLatest { lista ->
                _eventos.value = lista
            }
        }
    }

    // 🟢 Crear evento
    fun crearEvento(evento: EventoEntity) {
        viewModelScope.launch {
            repo.crearEvento(evento)
        }
    }

    // 🟢 Formatear fecha desde Long → String legible
    fun formatearFecha(timestamp: Long): String {
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formato.format(Date(timestamp))
    }

    // 🟢 Guardar evento localmente (como “descargado”)
    fun guardarEventoLocal(context: Context, evento: EventoEntity) {
        viewModelScope.launch {
            try {
                // Carpeta EventLive
                val carpeta = File(context.getExternalFilesDir(null), "EventLive")
                if (!carpeta.exists()) carpeta.mkdirs()

                // Copiar imagen si existe
                evento.imagenUri?.let { uriStr ->
                    val uri = Uri.parse(uriStr)
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val destino = File(carpeta, "evento_${evento.id}.jpg")
                    val outputStream = FileOutputStream(destino)
                    inputStream?.copyTo(outputStream)
                    outputStream.close()
                    inputStream?.close()
                }

                // Marcar evento como guardado en BD
                val eventoGuardado = evento.copy(isGuardado = true)
                repo.crearEvento(eventoGuardado)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 🟢 Obtener eventos guardados localmente
    fun obtenerGuardados(): List<EventoEntity> {
        return _eventos.value.filter { it.isGuardado }
    }
}
