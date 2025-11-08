package com.example.eventoapp.data.Model.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "eventoapp_db"
            )
                .fallbackToDestructiveMigration() // elimina datos si cambia la versión (solo en desarrollo)
                .build()
            INSTANCE = instance
            instance
        }
    }
}
