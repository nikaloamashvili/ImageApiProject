package com.example.imageapiproject.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pixabay.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: PixabayApiService = retrofit.create(PixabayApiService::class.java)
}
