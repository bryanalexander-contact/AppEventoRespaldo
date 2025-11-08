package com.example.eventoapp.data.Model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.eventoapp.data.Model.dao.UsuarioDao
import com.example.eventoapp.data.Model.dao.EventoDao
import com.example.eventoapp.data.Model.dao.InvitadoDao
import com.example.eventoapp.data.Model.dao.ComentarioDao
import com.example.eventoapp.data.Model.dao.LugarDao
import com.example.eventoapp.data.Model.dao.NotificacionDao
import com.example.eventoapp.data.Model.entities.UsuarioEntity
import com.example.eventoapp.data.Model.entities.EventoEntity
import com.example.eventoapp.data.Model.entities.InvitadoEntity
import com.example.eventoapp.data.Model.entities.ComentarioEntity
import com.example.eventoapp.data.Model.entities.LugarEntity
import com.example.eventoapp.data.Model.entities.NotificacionEntity



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
