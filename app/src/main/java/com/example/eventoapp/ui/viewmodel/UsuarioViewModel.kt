package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.local.entities.UsuarioEntity
import com.example.eventoapp.data.local.repository.UsuarioRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {

    private val _usuarioActual = MutableLiveData<UsuarioEntity?>()
    val usuarioActual: LiveData<UsuarioEntity?> = _usuarioActual

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            val usuario = UsuarioEntity(nombre = nombre, correo = correo, contrasena = contrasena)
            repo.registrarUsuario(usuario)
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _usuarioActual.value = repo.login(correo, contrasena)
        }
    }
}
