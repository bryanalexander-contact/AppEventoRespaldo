package com.example.eventoapp.network

// network/WeatherApi.kt
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric" // para Celsius
    ): Call<WeatherResponse>
}
