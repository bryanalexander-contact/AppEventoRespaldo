package com.example.eventoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // -----------------------
    // Cambia estas URLs por las tuyas reales (IP y puertos)
    // -----------------------
    private const val BASE_EVENTOS = "http://98.88.76.248:4001/"
    private const val BASE_USUARIOS = "http://98.88.76.248:4000/"

    private val retrofitEventos: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_EVENTOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofitUsuarios: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_USUARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eventoApi: EventoApi by lazy { retrofitEventos.create(EventoApi::class.java) }
    val usuarioApi: UsuarioApi by lazy { retrofitUsuarios.create(UsuarioApi::class.java) }

    // Weather
    private val retrofitWeather: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
