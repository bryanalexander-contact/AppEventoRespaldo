package com.example.eventoapp.data.Model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.eventoapp.data.Model.entities.UsuarioEntity

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun login(correo: String, contrasena: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    suspend fun obtenerTodos(): List<UsuarioEntity>
}
