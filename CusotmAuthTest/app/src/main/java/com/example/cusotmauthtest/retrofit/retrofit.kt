package com.example.cusotmauthtest.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService{
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse
}

fun getAuthApiInstance(
    BASE_URL: String = "https://kf3tbcjh-3000.inc1.devtunnels.ms/"
): AuthApiService? {
    var retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService = retrofit.create(AuthApiService::class.java)

    return apiService
}
