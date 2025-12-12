package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.network.EventoResponse
import com.example.eventoapp.network.UploadUtils.createPartFromString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EventoViewModel : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoResponse>>(emptyList())
    val eventos: StateFlow<List<EventoResponse>> = _eventos

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

    // Flag que indica si la última creación fue exitosa (true), fallida (false) o null (sin intento reciente)
    private val _lastCreateSuccess = MutableStateFlow<Boolean?>(null)
    val lastCreateSuccess: StateFlow<Boolean?> = _lastCreateSuccess

    init {
        obtenerEventos()
    }

    fun obtenerEventos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventoApi.getEventos()
                if (response.isSuccessful) {
                    _eventos.value = response.body() ?: emptyList()
                    _mensajeError.value = null
                    // debug
                    println("obtenerEventos: ${_eventos.value.size} eventos cargados")
                } else {
                    _mensajeError.value = "Error al obtener eventos: ${response.code()}"
                    println("obtenerEventos error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    // Mantengo el método JSON existente por compatibilidad
    fun crearEvento(token: String, request: EventoCreateRequest) {
        viewModelScope.launch {
            try {
                val tokenFinal = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = ApiClient.eventoApi.crearEvento(tokenFinal, request)
                if (response.isSuccessful) {
                    obtenerEventos()
                    _mensajeError.value = null
                    _lastCreateSuccess.value = true
                } else {
                    _mensajeError.value = "Error al crear evento: ${response.code()}"
                    _lastCreateSuccess.value = false
                    println("crearEvento error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
                _lastCreateSuccess.value = false
                e.printStackTrace()
            }
        }
    }

    // Nuevo: crear evento enviando multipart (imagen + campos)
    fun crearEventoMultipart(
        token: String,
        usuarioId: Int,
        nombre: String,
        descripcion: String,
        direccion: String,
        fecha: Long,
        duracionHoras: Int,
        creadorNombre: String,
        imagenPart: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val usuarioIdRB = createPartFromString(usuarioId.toString())
                val nombreRB = createPartFromString(nombre)
                val descRB = createPartFromString(descripcion)
                val dirRB = createPartFromString(direccion)
                val fechaRB = createPartFromString(fecha.toString())
                val durRB = createPartFromString(duracionHoras.toString())
                val creadorRB = createPartFromString(creadorNombre)

                val tokenFinal = if (token.startsWith("Bearer ")) token else "Bearer $token"

                val response = ApiClient.eventoApi.crearEventoMultipart(
                    tokenFinal,
                    imagenPart,
                    usuarioIdRB,
                    nombreRB,
                    descRB,
                    dirRB,
                    fechaRB,
                    durRB,
                    creadorRB
                )

                if (response.isSuccessful) {
                    // recargar lista solo si la creación fue OK
                    obtenerEventos()
                    _mensajeError.value = null
                    _lastCreateSuccess.value = true
                    println("crearEventoMultipart OK: ${response.body()}")
                } else {
                    _mensajeError.value = "Error al crear evento: ${response.code()}"
                    _lastCreateSuccess.value = false
                    println("crearEventoMultipart error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
                _lastCreateSuccess.value = false
                e.printStackTrace()
            }
        }
    }

    fun clearError() {
        _mensajeError.value = null
    }

    // Limpiar flag de creación (para que la UI pueda reiniciarlo)
    fun clearLastCreateFlag() {
        _lastCreateSuccess.value = null
    }

    fun formatearFecha(timestamp: Long): String {
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return formato.format(java.util.Date(timestamp))
    }
}
