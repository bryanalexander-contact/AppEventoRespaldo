package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.local.entities.EventoEntity
import com.example.eventoapp.data.local.repository.EventoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
}
