package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.Model.entities.UsuarioEntity
import com.example.eventoapp.data.Model.repository.UsuarioRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData

class UsuarioViewModel(private val repo: UsuarioRepository) : ViewModel() {

    private val _usuarioActual = MutableLiveData<UsuarioEntity?>()
    val usuarioActual: LiveData<UsuarioEntity?> = _usuarioActual

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val usuario = UsuarioEntity(
                    nombre = nombre,
                    correo = correo,
                    contrasena = contrasena
                )
                repo.registrarUsuario(usuario)
                _mensajeError.postValue("Usuario registrado correctamente")
            } catch (e: Exception) {
                _mensajeError.postValue("Error al registrar: ${e.message}")
            }
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val usuario = repo.login(correo, contrasena)
                if (usuario != null) {
                    _usuarioActual.postValue(usuario)
                    _mensajeError.postValue(null)
                } else {
                    _mensajeError.postValue("Credenciales incorrectas")
                }
            } catch (e: Exception) {
                _mensajeError.postValue("Error de login: ${e.message}")
            }
        }
    }

    fun logout() {
        _usuarioActual.postValue(null)
    }
}
