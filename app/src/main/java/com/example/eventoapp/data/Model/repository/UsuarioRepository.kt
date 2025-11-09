package com.example.eventoapp.data.Model.repository

import com.example.eventoapp.data.Model.dao.UsuarioDao
import com.example.eventoapp.data.Model.entities.UsuarioEntity

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun registrarUsuario(usuario: UsuarioEntity) {
        usuarioDao.insertar(usuario)
    }

    suspend fun login(correo: String, contrasena: String): UsuarioEntity? {
        return usuarioDao.login(correo, contrasena)
    }

    suspend fun obtenerTodos(): List<UsuarioEntity> {
        return usuarioDao.obtenerTodos()
    }
}
