package com.example.eventoapp.data.Model.repository

import com.example.eventoapp.data.Model.dao.UsuarioDao
import com.example.eventoapp.data.Model.entities.UsuarioEntity

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun insertarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertarUsuario(usuario)
    }

    suspend fun obtenerUsuarioPorCorreo(correo: String): UsuarioEntity? {
        return usuarioDao.obtenerUsuarioPorCorreo(correo)
    }

    suspend fun obtenerUsuarios(): List<UsuarioEntity> {
        return usuarioDao.obtenerUsuarios()
    }

    suspend fun eliminarUsuario(usuario: UsuarioEntity) {
        usuarioDao.eliminarUsuario(usuario)
    }

}
