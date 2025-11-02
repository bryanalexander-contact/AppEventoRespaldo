package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.NotificacionDao
import com.example.eventoapp.data.local.entities.NotificacionEntity

class NotificacionRepository(private val notificacionDao: NotificacionDao) {

    suspend fun insertarNotificacion(notificacion: NotificacionEntity) {
        notificacionDao.insertarNotificacion(notificacion)
    }

    suspend fun obtenerNotificaciones(): List<NotificacionEntity> {
        return notificacionDao.obtenerNotificaciones()
    }

    suspend fun obtenerNotificacionPorId(id: Int): NotificacionEntity? {
        return notificacionDao.obtenerNotificacionPorId(id)
    }

    suspend fun actualizarNotificacion(notificacion: NotificacionEntity) {
        notificacionDao.actualizarNotificacion(notificacion)
    }

    suspend fun eliminarNotificacion(notificacion: NotificacionEntity) {
        notificacionDao.eliminarNotificacion(notificacion)
    }

    suspend fun obtenerNotificacionesNoLeidas(): List<NotificacionEntity> {
        return notificacionDao.obtenerNotificacionesNoLeidas()
    }
}
