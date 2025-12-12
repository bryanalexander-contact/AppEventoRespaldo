package com.example.eventoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.eventoapp.network.ApiClient
import com.example.eventoapp.network.LoginRequest
import com.example.eventoapp.network.RegisterRequest
import com.example.eventoapp.network.UsuarioNetwork
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {

    private val _usuarioActual = MutableLiveData<UsuarioNetwork?>()
    val usuarioActual: LiveData<UsuarioNetwork?> = _usuarioActual

    private val _mensajeError = MutableLiveData<String?>()
    val mensajeError: LiveData<String?> = _mensajeError

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    fun registrarUsuario(nombre: String, correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val req = RegisterRequest(nombre, correo, contrasena)
                val response = ApiClient.usuarioApi.register(req)
                if (response.isSuccessful) {
                    val user = response.body()?.user
                    if (user != null) {
                        _mensajeError.postValue("Usuario registrado correctamente")
                        _usuarioActual.postValue(user)
                    } else {
                        _mensajeError.postValue("Registro exitoso pero no se devolvió usuario")
                    }
                } else {
                    if (response.code() == 409) {
                        _mensajeError.postValue("El correo ya existe")
                    } else {
                        _mensajeError.postValue("Error al registrar: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _mensajeError.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            try {
                val req = LoginRequest(correo, contrasena)
                val response = ApiClient.usuarioApi.login(req)
                if (response.isSuccessful) {
                    val body = response.body()
                    val tokenRec = body?.token
                    val user = body?.user
                    if (tokenRec != null) {
                        _token.postValue(tokenRec)
                        if (user != null) {
                            // ahora guardamos el usuario real con su id
                            _usuarioActual.postValue(user)
                        } else {
                            // fallback razonable: usar correo si el user no vino (muy raro)
                            _usuarioActual.postValue(UsuarioNetwork(id = -1, nombre = correo, correo = correo, rol = null))
                        }
                        _mensajeError.postValue(null)
                    } else {
                        _mensajeError.postValue("No se recibió token")
                    }
                } else {
                    if (response.code() == 401) {
                        _mensajeError.postValue("Credenciales inválidas")
                    } else {
                        _mensajeError.postValue("Error en login: ${response.code()}")
                    }
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
