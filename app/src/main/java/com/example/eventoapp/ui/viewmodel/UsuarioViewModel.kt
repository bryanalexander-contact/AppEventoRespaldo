package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventoapp.data.Model.entities.UsuarioEntity
import com.example.eventoapp.data.Model.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Estado del formulario (para Compose)
    private val _mensajeEstado = MutableStateFlow("")
    val mensajeEstado: StateFlow<String> = _mensajeEstado

    fun registrarUsuario(nombre: String, correo: String, contrasena: String, tipo: String) {
        viewModelScope.launch {
            // Validaciones simples
            if (nombre.isBlank() || correo.isBlank() || contrasena.isBlank()) {
                _mensajeEstado.value = "Todos los campos son obligatorios"
                return@launch
            }

            val usuarioExistente = repository.obtenerUsuarioPorCorreo(correo)
            if (usuarioExistente != null) {
                _mensajeEstado.value = "El correo ya está registrado"
                return@launch
            }

            val nuevoUsuario = UsuarioEntity(
                nombre_user = nombre,
                correo_user = correo,
                contrasena_user = contrasena,
                tipo_user = tipo,
                fechaRegistro = java.util.Date()
            )

            repository.insertarUsuario(nuevoUsuario)
            _mensajeEstado.value = "Usuario registrado correctamente ✅"
        }
    }
}
