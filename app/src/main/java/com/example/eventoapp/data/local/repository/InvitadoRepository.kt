package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.InvitadoDao
import com.example.eventoapp.data.local.entities.InvitadoEntity

class InvitadoRepository(private val invitadoDao: InvitadoDao) {

    suspend fun insertarInvitado(invitado: InvitadoEntity) {
        invitadoDao.insertarInvitado(invitado)
    }

    suspend fun obtenerInvitadosPorEvento(eventoId: Int): List<InvitadoEntity> {
        return invitadoDao.obtenerInvitadosPorEvento(eventoId)
    }

    suspend fun obtenerEventosPorUsuario(usuarioId: Int): List<InvitadoEntity> {
        return invitadoDao.obtenerEventosPorUsuario(usuarioId)
    }

    suspend fun actualizarConfirmacion(id: Int, confirmado: Boolean) {
        invitadoDao.actualizarConfirmacion(id, confirmado)
    }

    suspend fun eliminarInvitado(invitado: InvitadoEntity) {
        invitadoDao.eliminarInvitado(invitado)
    }
}
