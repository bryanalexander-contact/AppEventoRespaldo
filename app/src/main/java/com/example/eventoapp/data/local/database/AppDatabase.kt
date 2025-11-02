package com.example.eventoapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventoapp.data.local.dao.UsuarioDao
import com.example.eventoapp.data.local.dao.EventoDao
import com.example.eventoapp.data.local.dao.InvitadoDao
import com.example.eventoapp.data.local.dao.ComentarioDao
import com.example.eventoapp.data.local.dao.LugarDao
import com.example.eventoapp.data.local.dao.NotificacionDao
import com.example.eventoapp.data.local.entities.UsuarioEntity
import com.example.eventoapp.data.local.entities.EventoEntity
import com.example.eventoapp.data.local.entities.InvitadoEntity
import com.example.eventoapp.data.local.entities.ComentarioEntity
import com.example.eventoapp.data.local.entities.LugarEntity
import com.example.eventoapp.data.local.entities.NotificacionEntity



@Database(
    entities = [
        UsuarioEntity::class,
        EventoEntity::class,
        ComentarioEntity::class,
        LugarEntity::class,
        NotificacionEntity::class,
        InvitadoEntity::class // 👈 agregada aquí
    ],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
    abstract fun comentarioDao(): ComentarioDao
    abstract fun lugarDao(): LugarDao
    abstract fun notificacionDao(): NotificacionDao
    abstract fun invitadoDao(): InvitadoDao // 👈 también agregado aquí
}
