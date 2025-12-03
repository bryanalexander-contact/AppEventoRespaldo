package com.example.eventoapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequest(val nombre: String, val correo: String, val contrasena: String)
data class RegisterResponse(val user: UsuarioNetwork) // según tu node: res.status(201).json({ user: result.rows[0] })

data class LoginRequest(val correo: String, val contrasena: String)
data class LoginResponse(val token: String)

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
