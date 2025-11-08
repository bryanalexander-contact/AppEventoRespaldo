package com.example.eventoapp.data.Model.dao

import androidx.room.*
import com.example.eventoapp.data.Model.entities.InvitadoEntity as Invitado

@Dao
interface InvitadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarInvitado(invitado: Invitado)

    @Query("SELECT * FROM invitados WHERE eventoID = :eventoId")
    suspend fun obtenerInvitadosPorEvento(eventoId: Int): List<Invitado>

    @Query("SELECT * FROM invitados WHERE usuarioID = :usuarioId")
    suspend fun obtenerEventosPorUsuario(usuarioId: Int): List<Invitado>

    @Query("UPDATE invitados SET confirmado = :confirmado WHERE idInvitado = :id")
    suspend fun actualizarConfirmacion(id: Int, confirmado: Boolean)

    @Delete
    suspend fun eliminarInvitado(invitado: Invitado)
}
