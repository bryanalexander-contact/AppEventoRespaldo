package com.example.eventoapp.data.Model.dao

import androidx.room.*
import com.example.eventoapp.data.Model.entities.UsuarioEntity as Usuario

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