package com.example.imageapiproject.networking

import com.example.imageapiproject.model.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {
    @GET("?key=44259285-16cfce901e96ccfda35bacb8e")
    suspend fun searchImages(@Query("q") query: String, @Query("page") page: Int): PixabayResponse
}


