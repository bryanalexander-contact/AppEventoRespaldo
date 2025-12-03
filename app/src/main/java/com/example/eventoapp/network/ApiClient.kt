package com.example.eventoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // -----------------------
    // Cambia estas URLs por las tuyas reales (IP y puertos)
    // En tu ejemplo: eventos -> 34.224.85.71:4001, usuarios -> 34.224.85.71:4000
    // -----------------------
    private const val BASE_EVENTOS = "http://13.222.189.41:4001/" // <- reemplaza si necesitas https o dominio
    private const val BASE_USUARIOS = "http://13.222.189.41:4000/"

    // Retrofit para eventos
    private val retrofitEventos: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_EVENTOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para usuarios (auth)
    private val retrofitUsuarios: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_USUARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val eventoApi: EventoApi by lazy { retrofitEventos.create(EventoApi::class.java) }
    val usuarioApi: UsuarioApi by lazy { retrofitUsuarios.create(UsuarioApi::class.java) }

    // Weather (opcional)
    private val retrofitWeather: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApi: WeatherApi by lazy { retrofitWeather.create(WeatherApi::class.java) }
}
