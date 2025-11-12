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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class EventoViewModel(private val repo: EventoRepository) : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoEntity>>(emptyList())
    val eventos: StateFlow<List<EventoEntity>> = _eventos

    init {
        cargarEventos()
    }

    fun cargarEventos() {
        viewModelScope.launch {
            repo.listarEventos().collectLatest { lista ->
                _eventos.value = lista
            }
        }
    }

    fun crearEvento(evento: EventoEntity) {
        viewModelScope.launch {
            repo.crearEvento(evento)
        }
    }

    fun formatearFecha(timestamp: Long): String {
        val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return formato.format(Date(timestamp))
    }

    /**
     * 🔹 Guardar imagen del evento en galería real del teléfono
     */
    fun guardarEventoLocal(context: Context, evento: EventoEntity) {
        viewModelScope.launch {
            try {
                evento.imagenUri?.let { uriStr ->
                    val inputStream = context.contentResolver.openInputStream(Uri.parse(uriStr))
                    val filename = "evento_${evento.id}.jpg"

                    if (inputStream != null) {
                        val outputStream: OutputStream? =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                val contentValues = ContentValues().apply {
                                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EventLive")
                                }
                                val uri: Uri? = context.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    contentValues
                                )
                                uri?.let { context.contentResolver.openOutputStream(it) }
                            } else {
                                // Android < Q
                                val carpeta = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                    "EventLive"
                                )
                                if (!carpeta.exists()) carpeta.mkdirs()
                                val file = File(carpeta, filename)
                                file.outputStream()
                            }

                        outputStream?.use { inputStream.copyTo(it) }
                        inputStream.close()
                    }
                }

                // Marcar evento como guardado en BD
                val eventoGuardado = evento.copy(isGuardado = true)
                repo.crearEvento(eventoGuardado)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun obtenerGuardados(): List<EventoEntity> {
        return _eventos.value.filter { it.isGuardado }
    }
}
