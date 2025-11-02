package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.ComentarioDao
import com.example.eventoapp.data.local.entities.ComentarioEntity

class ComentarioRepository(private val comentarioDao: ComentarioDao) {

    suspend fun insertarComentario(comentario: ComentarioEntity) {
        comentarioDao.insertarComentario(comentario)
    }

    suspend fun obtenerComentarioPorId(id: Int): ComentarioEntity? {
        return comentarioDao.obtenerComentarioPorId(id)
    }

    suspend fun obtenerComentariosPorEvento(idEvento: Int): List<ComentarioEntity> {
        return comentarioDao.obtenerComentariosPorEvento(idEvento)
    }

    suspend fun actualizarComentario(comentario: ComentarioEntity) {
        comentarioDao.actualizarComentario(comentario)
    }

    suspend fun eliminarComentario(comentario: ComentarioEntity) {
        comentarioDao.eliminarComentario(comentario)
    }
}
