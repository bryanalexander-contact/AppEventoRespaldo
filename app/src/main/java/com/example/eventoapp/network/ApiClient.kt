package com.example.eventoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    // Retrofit principal para tu backend de eventos
    private val retrofitBackend: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://tu-backend.com/") // Reemplaza con tu URL real de backend
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // APIs de tu backend
    val eventoApi: EventoApi by lazy { retrofitBackend.create(EventoApi::class.java) }
    val usuarioApi: UsuarioApi by lazy { retrofitBackend.create(UsuarioApi::class.java) }

    // Retrofit para OpenWeatherMap
    private val retrofitWeather: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/") // URL base de clima
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Weather API
    val weatherApi: WeatherApi by lazy { retrofitWeather.create(WeatherApi::class.java) }
}
