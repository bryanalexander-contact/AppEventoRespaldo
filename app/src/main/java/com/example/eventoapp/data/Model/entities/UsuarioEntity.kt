package com.example.eventoapp.data.Model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val idUser: Int = 0,
    val nombre_user: String,
    val correo_user: String,
    val contrasena_user: String,
    val tipo_user: String, // "normal" o "admin"
    val fechaRegistro: Date
)

package com.example.eventoapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val rol: String = "usuario" // por ahora solo usuario normal
)
