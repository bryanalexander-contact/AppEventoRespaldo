package com.example.eventoapp.network

import retrofit2.http.GET

interface DummyApi {
    // GET https://dummyjson.com/quotes/random
    @GET("quotes/random")
    suspend fun getRandomQuote(): Quote
}
