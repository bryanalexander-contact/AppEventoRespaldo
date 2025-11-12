package com.example.eventoapp.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.data.Model.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class EventoViewModel(private val repo: EventoRepository) : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoEntity>>(emptyList())
    val eventos: StateFlow<List<EventoEntity>> = _eventos

    init {
        viewModelScope.launch {
            repo.listarEventos().collect { lista ->
                _eventos.value = lista
            }
        }
    }

    fun crearEvento(evento: EventoEntity) {
        viewModelScope.launch {
            repo.crearEvento(evento)
        }
    }

    // 🟢 Copiar imagen de un evento a la galería manualmente (si fuera necesario)
    fun guardarEventoLocal(context: Context, evento: EventoEntity) {
        viewModelScope.launch {
            try {
                evento.imagenUri?.let { uriStr ->
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(uriStr))
                    val filename = "evento_${evento.id}.jpg"

                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EventLive")
                    }

                    val imageUri: Uri? = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                    val outputStream: OutputStream? = imageUri?.let {
                        context.contentResolver.openOutputStream(it)
                    }

                    if (inputStream != null && outputStream != null) {
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun formatearFecha(timestamp: Long): String {
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formato.format(Date(timestamp))
    }
}
