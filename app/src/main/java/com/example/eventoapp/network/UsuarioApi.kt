package com.example.eventoapp.network

import retrofit2.http.*
import retrofit2.Response

data class LoginRequest(val correo: String, val contrasena: String)
data class RegisterRequest(val nombre: String, val correo: String, val contrasena: String)
data class LoginResponse(val token: String)

interface UsuarioApi {

    @POST("/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<Unit>

    @POST("/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>
}
