package com.example.eventoapp.data.Model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.eventoapp.data.Model.dao.*
import com.example.eventoapp.data.Model.entities.*

@Database(
    entities = [
        UsuarioEntity::class,
        EventoEntity::class,
        ComentarioEntity::class,
        NotificacionEntity::class,
        InvitadoEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class EventoDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun eventoDao(): EventoDao
    abstract fun comentarioDao(): ComentarioDao
    abstract fun notificacionDao(): NotificacionDao
    abstract fun invitadoDao(): InvitadoDao
}
