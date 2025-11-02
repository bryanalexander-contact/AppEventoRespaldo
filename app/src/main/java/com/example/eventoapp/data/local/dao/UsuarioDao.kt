package com.example.eventoapp.data.local.dao

import androidx.room.*
import com.example.eventoapp.data.local.entities.UsuarioEntity as Usuario

@Dao
interface UsuarioDao {

    // Insertar usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: Usuario)

    // Obtener todos los usuarios
    @Query("SELECT * FROM usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    // Buscar usuario por correo
    @Query("SELECT * FROM usuarios WHERE correo_user = :correo LIMIT 1")
    suspend fun obtenerUsuarioPorCorreo(correo: String): Usuario?

    // Eliminar usuario
    @Delete
    suspend fun eliminarUsuario(usuario: Usuario)
}