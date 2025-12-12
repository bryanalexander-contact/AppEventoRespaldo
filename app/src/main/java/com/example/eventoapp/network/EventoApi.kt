package com.example.eventoapp.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import com.google.gson.annotations.SerializedName

data class EventoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("usuarioid") val usuarioId: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("fecha") val fecha: Long,
    @SerializedName("duracionhoras") val duracionHoras: Int,
    @SerializedName("imagenuri") val imagenUri: String?,
    @SerializedName("creadornombre") val creadorNombre: String?,
    @SerializedName("isguardado") val isGuardado: Boolean?
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

    @Multipart
    @POST("eventos")
    suspend fun crearEventoMultipart(
        @Header("Authorization") token: String,
        @Part imagen: MultipartBody.Part?,
        @Part("usuarioId") usuarioId: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("descripcion") descripcion: RequestBody,
        @Part("direccion") direccion: RequestBody,
        @Part("fecha") fecha: RequestBody,
        @Part("duracionHoras") duracionHoras: RequestBody,
        @Part("creadorNombre") creadorNombre: RequestBody
    ): Response<EventoResponse>
}
