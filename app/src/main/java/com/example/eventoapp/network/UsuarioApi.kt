package com.example.eventoapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(val nombre: String, val correo: String, val contrasena: String)
data class RegisterResponse(val user: UsuarioNetwork)

data class LoginRequest(val correo: String, val contrasena: String)
// Ahora login devuelve token + user
data class LoginResponse(val token: String, val user: UsuarioNetwork?)

data class UsuarioNetwork(
    val id: Int,
    val nombre: String,
    val correo: String,
    val rol: String?
)

interface UsuarioApi {
    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}
