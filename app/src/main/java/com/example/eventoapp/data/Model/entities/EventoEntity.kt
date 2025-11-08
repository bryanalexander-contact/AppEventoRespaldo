package com.example.eventoapp.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "evento")
data class EventoEntity(
    @PrimaryKey(autoGenerate = true) val idEvento: Int = 0,
    val usuarioId: Int, // 🔹 Referencia al usuario dueño del evento
    val titulo: String,
    val descripcion: String,
    val fecha: Date,
    val estado: String,
    val imagen: ByteArray?
)

