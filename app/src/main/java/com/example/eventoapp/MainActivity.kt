package com.example.eventoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.eventoapp.data.Model.database.EventoDatabase
import com.example.eventoapp.data.Model.repository.UsuarioRepository
import com.example.eventoapp.ui.screens.RegistroUsuarioScreen
import com.example.eventoapp.ui.viewmodel.UsuarioViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            EventoDatabase::class.java,
            "evento_db"
        ).build()

        val usuarioRepository = UsuarioRepository(db.usuarioDao())
        val usuarioViewModel = UsuarioViewModel(usuarioRepository)

        setContent {
            RegistroUsuarioScreen(usuarioViewModel)
        }
    }
}
