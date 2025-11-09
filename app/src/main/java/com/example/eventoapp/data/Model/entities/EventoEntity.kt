package com.example.eventoapp.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: Long, // se guardará como timestamp
    val duracionHoras: Int,
    val imagenUri: String?,
    val creadorNombre: String, // se obtiene del usuario logueado
    val isGuardado: Boolean = false // si el usuario lo guardó en su carpeta local
)
