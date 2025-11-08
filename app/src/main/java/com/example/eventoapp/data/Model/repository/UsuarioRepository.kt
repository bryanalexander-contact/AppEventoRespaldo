package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.UsuarioDao
import com.example.eventoapp.data.local.entities.UsuarioEntity

class UsuarioRepository(private val usuarioDao: UsuarioDao) {
    suspend fun registrarUsuario(usuario: UsuarioEntity) = usuarioDao.insertar(usuario)
    suspend fun login(correo: String, contrasena: String) = usuarioDao.login(correo, contrasena)
}
