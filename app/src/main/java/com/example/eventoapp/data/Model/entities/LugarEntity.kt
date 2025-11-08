package com.example.eventoapp.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lugar")
data class LugarEntity(
    @PrimaryKey(autoGenerate = true) val idLugar: Int = 0,
    val nombre_lugar: String,
    val descripcion: String,
    val fotoUrl: String
)

