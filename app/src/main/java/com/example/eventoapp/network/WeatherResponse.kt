package com.example.eventoapp.network

// Modelo sencillo para recibir la respuesta de OpenWeatherMap
data class WeatherResponse(
    val main: Main
)

data class Main(
    val temp: Float
)
