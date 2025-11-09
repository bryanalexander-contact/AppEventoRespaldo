package com.example.eventoapp.data.Model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventoapp.data.Model.dao.EventoDao
import com.example.eventoapp.data.Model.dao.UsuarioDao
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.data.Model.entities.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, EventoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
}
