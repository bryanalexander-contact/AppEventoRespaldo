package com.example.eventoapp.data.local.dao

import androidx.room.*
import com.example.eventoapp.data.local.entities.NotificacionEntity as Notificacion

@Dao
interface NotificacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarNotificacion(notificacion: Notificacion)

    @Query("SELECT * FROM notificaciones")
    suspend fun obtenerNotificaciones(): List<Notificacion>

    @Query("SELECT * FROM notificaciones WHERE id = :id")
    suspend fun obtenerNotificacionPorId(id: Int): Notificacion?

    @Query("SELECT * FROM notificaciones WHERE leido = 0")
    suspend fun obtenerNotificacionesNoLeidas(): List<Notificacion>

    @Update
    suspend fun actualizarNotificacion(notificacion: Notificacion)

    @Delete
    suspend fun eliminarNotificacion(notificacion: Notificacion)
}
