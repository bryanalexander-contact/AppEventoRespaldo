package com.example.eventoapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DummyClient {
    private const val BASE_URL = "https://dummyjson.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val dummyApi: DummyApi by lazy { retrofit.create(DummyApi::class.java) }
}
