package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.LoginRequest
import com.example.eventoapp.network.RegisterRequest
import com.example.eventoapp.data.Model.entities.UsuarioEntity
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val _usuarioActual = MutableLiveData<UsuarioEntity?>()
    val usuarioActual: LiveData<UsuarioEntity?> = _usuarioActual

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    /**
     * Registrar usuario en microservicio
     */
    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(nombre, correo, contrasena)
                val response = ApiClient.usuarioApi.register(request)

                if (response.isSuccessful) {
                    _mensajeError.postValue("Usuario registrado correctamente")
                } else {
                    _mensajeError.postValue("Error al registrar: ${response.code()}")
                }
            } catch (e: Exception) {
                _mensajeError.postValue("Error de red: ${e.message}")
            }
        }
    }

    /**
     * Login de usuario en microservicio
     */
    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(correo, contrasena)
                val response = ApiClient.usuarioApi.login(request)

                if (response.isSuccessful) {
                    val tokenRecibido = response.body()?.token
                    if (tokenRecibido != null) {
                        _token.postValue(tokenRecibido)
                        _usuarioActual.postValue(UsuarioEntity(nombre = correo, correo = correo, contrasena = contrasena))
                        _mensajeError.postValue(null)
                    } else {
                        _mensajeError.postValue("No se recibió token")
                    }
                } else {
                    _mensajeError.postValue("Credenciales incorrectas: ${response.code()}")
                }

            } catch (e: Exception) {
                _mensajeError.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun logout() {
        _usuarioActual.postValue(null)
        _token.postValue(null)
    }
}
