package com.example.eventoapp.data.Model.dao

import androidx.room.*
import com.example.eventoapp.data.Model.entities.LugarEntity as Lugar

@Dao
interface LugarDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarLugar(lugar: Lugar)

    @Query("SELECT * FROM lugares")
    suspend fun obtenerLugares(): List<Lugar>

    @Query("SELECT * FROM lugares WHERE id_lugar = :id")
    suspend fun obtenerLugarPorId(id: Int): Lugar?

    @Update
    suspend fun actualizarLugar(lugar: Lugar)

    @Delete
    suspend fun eliminarLugar(lugar: Lugar)
}
