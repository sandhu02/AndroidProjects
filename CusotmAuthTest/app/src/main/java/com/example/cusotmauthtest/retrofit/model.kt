package com.example.cusotmauthtest.retrofit

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
)