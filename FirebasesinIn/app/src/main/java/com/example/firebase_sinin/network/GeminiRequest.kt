package com.example.firebase_sinin.network

data class GeminiContentRequest (
    val contents : List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)
