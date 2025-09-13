package com.example.cusotmauthtest.retrofit


data class VerifyTokenResponse(
    val success: Boolean,
    val message: String
)

data class RegisterRequest(
    val name: String,
    val username: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token : String
)

data class LogoutResponse(
    val success: Boolean,
    val message: String
)

data class Video(
    val url : String,
    val publicId : String ,
    val title : String ,
    val description : String
)
data class VideoResponse(
    val success: Boolean,
    val message: String
)
data class DeleteVideoResponse(
    val success: Boolean,
    val message: String
)

data class User(
    val name: String,
    val username: String,
    val password: String
)