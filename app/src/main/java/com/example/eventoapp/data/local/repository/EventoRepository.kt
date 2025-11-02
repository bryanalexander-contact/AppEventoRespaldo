package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.EventoDao
import com.example.eventoapp.data.local.entities.EventoEntity

class EventoRepository(private val eventoDao: EventoDao) {

    suspend fun insertarEvento(evento: EventoEntity) {
        eventoDao.insertarEvento(evento)
    }

    suspend fun obtenerEventos(): List<EventoEntity> {
        return eventoDao.obtenerEventos()
    }

    suspend fun obtenerEventoPorId(id: Int): EventoEntity? {
        return eventoDao.obtenerEventoPorId(id)
    }

    suspend fun obtenerEventosPorUsuario(idUsuario: Int): List<EventoEntity> {
        return eventoDao.obtenerEventosPorUsuario(idUsuario)
    }

    suspend fun actualizarEvento(evento: EventoEntity) {
        eventoDao.actualizarEvento(evento)
    }

    suspend fun eliminarEvento(evento: EventoEntity) {
        eventoDao.eliminarEvento(evento)
    }
}

