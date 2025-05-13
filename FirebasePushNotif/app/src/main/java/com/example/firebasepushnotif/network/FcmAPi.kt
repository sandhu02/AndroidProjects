package com.example.firebasepushnotif.network

import retrofit2.http.Body
import retrofit2.http.POST

interface FcmAPi {
    @POST("")
    suspend fun sendMessage(
        @Body body : SendMessageDTO
    )

    @POST("/broadcast")
    suspend fun broadcast(
        @Body body : SendMessageDTO
    )
}