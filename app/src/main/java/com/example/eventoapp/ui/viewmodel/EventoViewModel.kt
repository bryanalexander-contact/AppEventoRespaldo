package com.example.eventoapp.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.Evento        // API
import com.example.eventoapp.data.Model.entities.EventoEntity   // Local
import com.example.eventoapp.data.Model.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class EventoViewModel(private val repo: EventoRepository) : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoEntity>>(emptyList())
    val eventos: StateFlow<List<EventoEntity>> = _eventos

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

    init {
        obtenerEventos()
    }

    /**
     * Obtener eventos desde microservicio y local
     */
    fun obtenerEventos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventoApi.getEventos()
                if (response.isSuccessful) {
                    val eventosApi = response.body()?.map { e ->
                        EventoEntity(
                            id = e.id,
                            usuarioId = e.usuarioId,
                            nombre = e.nombre,
                            descripcion = e.descripcion,
                            direccion = e.direccion,
                            fecha = e.fecha,
                            duracionHoras = e.duracionHoras,
                            imagenUri = e.imagenUri,
                            creadorNombre = e.creadorNombre
                        )
                    } ?: emptyList()
                    _eventos.value = eventosApi
                } else {
                    _mensajeError.value = "Error al obtener eventos: ${response.code()}"
                }

            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            }
        }
    }

    /**
     * Crear evento y enviarlo al microservicio
     */
    fun crearEvento(token: String, evento: EventoEntity) {

        val eventoApi = Evento(
            id = evento.id,
            usuarioId = evento.usuarioId,
            nombre = evento.nombre,
            descripcion = evento.descripcion,
            direccion = evento.direccion,
            fecha = evento.fecha,
            duracionHoras = evento.duracionHoras,
            imagenUri = evento.imagenUri,
            creadorNombre = evento.creadorNombre
        )

        viewModelScope.launch {
            try {
                val response = ApiClient.eventoApi.crearEvento("Bearer $token", eventoApi)
                if (response.isSuccessful) {
                    // Actualizar lista local
                    obtenerEventos()
                } else {
                    _mensajeError.value = "Error al crear evento: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            }
        }
    }

    /**
     * Guardar imagen en almacenamiento local
     */
    fun guardarEventoLocal(context: Context, evento: EventoEntity) {
        viewModelScope.launch {
            try {
                evento.imagenUri?.let { uriStr ->

                    val uri: Uri = when {
                        uriStr.startsWith("content://") -> Uri.parse(uriStr)
                        uriStr.startsWith("file://") -> Uri.parse(uriStr)
                        uriStr.startsWith("/") -> Uri.fromFile(File(uriStr))
                        else -> Uri.parse(uriStr)
                    }

                    val inputStream = context.contentResolver.openInputStream(uri)
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
