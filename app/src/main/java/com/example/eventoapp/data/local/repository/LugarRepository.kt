package com.example.eventoapp.data.local.repository

import com.example.eventoapp.data.local.dao.LugarDao
import com.example.eventoapp.data.local.entities.LugarEntity

class LugarRepository(private val lugarDao: LugarDao) {

    suspend fun insertarLugar(lugar: LugarEntity) {
        lugarDao.insertarLugar(lugar)
    }

    suspend fun obtenerLugares(): List<LugarEntity> {
        return lugarDao.obtenerLugares()
    }

    suspend fun obtenerLugarPorId(id: Int): LugarEntity? {
        return lugarDao.obtenerLugarPorId(id)
    }

    suspend fun actualizarLugar(lugar: LugarEntity) {
        lugarDao.actualizarLugar(lugar)
    }

    suspend fun eliminarLugar(lugar: LugarEntity) {
        lugarDao.eliminarLugar(lugar)
    }
}
