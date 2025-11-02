package com.example.eventoapp.data.local.dao

import androidx.room.*
import com.example.eventoapp.data.local.entities.EventoEntity as Evento

@Dao
interface EventoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarEvento(evento: Evento)

    @Query("SELECT * FROM eventos")
    suspend fun obtenerEventos(): List<Evento>

    @Query("SELECT * FROM eventos WHERE idEvento = :id")
    suspend fun obtenerEventoPorId(id: Int): Evento?

    @Query("SELECT * FROM evento WHERE usuarioId = :idUsuario")
    suspend fun obtenerEventosPorUsuario(idUsuario: Int): List<EventoEntity>

    @Update
    suspend fun actualizarEvento(evento: Evento)

    @Delete
    suspend fun eliminarEvento(evento: Evento)
}
