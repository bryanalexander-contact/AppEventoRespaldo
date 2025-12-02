package com.example.eventoapp.network

import retrofit2.http.*
import retrofit2.Response

data class Evento(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val direccion: String,
    val fecha: Long,
    val duracionHoras: Int,
    val imagenUri: String?,
    val creadorNombre: String,
    val usuarioId: Int
)

interface EventoApi {

    @GET("/eventos")
    suspend fun getEventos(): Response<List<Evento>>

    @POST("/eventos")
    suspend fun crearEvento(
        @Header("Authorization") token: String,
        @Body body: Evento
    ): Response<Evento>
}
