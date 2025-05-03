package com.example.firebase_sinin.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiContentRequest
    ): Response<GeminiContentResponse>
}

private const val BASE_URL = "https://generativelanguage.googleapis.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

object GeminiApi {
    val retrofitService : GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}