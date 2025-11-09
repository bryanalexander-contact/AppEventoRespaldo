package com.example.eventoapp.data.Model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventoapp.data.Model.entities.EventoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventoDao {
    @Insert
    suspend fun insertarEvento(evento: EventoEntity)

    @Query("SELECT * FROM eventos ORDER BY id DESC")
    fun obtenerEventos(): Flow<List<EventoEntity>>
}
