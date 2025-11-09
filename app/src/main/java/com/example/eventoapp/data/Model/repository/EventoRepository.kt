package com.example.eventoapp.data.Model.repository

import com.example.eventoapp.data.Model.dao.EventoDao
import com.example.eventoapp.data.Model.entities.EventoEntity
import kotlinx.coroutines.flow.Flow

class EventoRepository(private val eventoDao: EventoDao) {

    suspend fun crearEvento(evento: EventoEntity) {
        eventoDao.insertarEvento(evento)
    }

    fun listarEventos(): Flow<List<EventoEntity>> {
        return eventoDao.obtenerEventos()
    }
}
