package com.example.eventoapp.network

import retrofit2.Response
import retrofit2.http.*

data class EventoResponse(
    val id: Int,
    val usuarioId: Int,
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: Long,
    val duracionHoras: Int,
    val imagenUri: String?,
    val creadorNombre: String,
    val isGuardado: Boolean?
)

data class EventoCreateRequest(
    val usuarioId: Int,
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: Long,
    val duracionHoras: Int,
    val imagenUri: String?,
    val creadorNombre: String
)

interface EventoApi {

    @GET("eventos")
    suspend fun getEventos(): Response<List<EventoResponse>>

    @POST("eventos")
    suspend fun crearEvento(
        @Header("Authorization") token: String,
        @Body body: EventoCreateRequest
    ): Response<EventoResponse>
}
