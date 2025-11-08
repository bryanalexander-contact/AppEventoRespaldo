package com.example.eventoapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventoapp.data.local.dao.EventoDao
import com.example.eventoapp.data.local.dao.UsuarioDao
import com.example.eventoapp.data.local.entities.EventoEntity
import com.example.eventoapp.data.local.entities.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, EventoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
}
