package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.EventoCreateRequest
import com.example.eventoapp.network.EventoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventoViewModel : ViewModel() {

    private val _eventos = MutableStateFlow<List<EventoResponse>>(emptyList())
    val eventos: StateFlow<List<EventoResponse>> = _eventos

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError

    init {
        obtenerEventos()
    }

    fun obtenerEventos() {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventoApi.getEventos()
                if (response.isSuccessful) {
                    _eventos.value = response.body() ?: emptyList()
                } else {
                    _mensajeError.value = "Error al obtener eventos: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            }
        }
    }

    fun crearEvento(token: String, request: EventoCreateRequest) {
        viewModelScope.launch {
            try {
                val response = ApiClient.eventoApi.crearEvento("Bearer $token", request)
                if (response.isSuccessful) {
                    // refrescar lista
                    obtenerEventos()
                } else {
                    _mensajeError.value = "Error al crear evento: ${response.code()}"
                }
            } catch (e: Exception) {
                _mensajeError.value = "Error de red: ${e.message}"
            }
        }
    }

    fun clearError() {
        _mensajeError.value = null
    }

    fun formatearFecha(timestamp: Long): String {
        val formato = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return formato.format(java.util.Date(timestamp))
    }
}
