package com.example.eventoapp.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invitados")
data class InvitadoEntity(
    @PrimaryKey(autoGenerate = true) val idInvitado: Int = 0,
    val eventoID: Int,     // Evento al que se invita
    val usuarioID: Int,    // Usuario invitado
    val confirmado: Boolean = false // Asistencia confirmada o no
)
