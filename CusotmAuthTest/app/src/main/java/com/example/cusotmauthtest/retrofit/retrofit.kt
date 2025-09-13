package com.example.cusotmauthtest.retrofit

import android.content.Context
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface AuthApiService{
    @GET("/")
    suspend fun verifyToken() : VerifyTokenResponse

    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @GET("/videos")
    suspend fun getVideos(): List<Video>

    @Multipart
    @POST("/videos")
    suspend fun uploadVideo(
        @Part video: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): VideoResponse

    @DELETE("/videos")
    suspend fun deleteVideo(@Query("publicId") publicId: String) : DeleteVideoResponse

    @GET("/users")
    suspend fun getUsers() : List<User>

    @DELETE("/logout")
    suspend fun logout() : LogoutResponse
}

const val BASE_URL: String = "https://kf3tbcjh-3000.inc1.devtunnels.ms/"

fun getAuthApiInstance(
    context : Context
): AuthApiService? {
    val token = getToken(context) ?: ""

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService = retrofit.create(AuthApiService::class.java)

    return apiService
}
