package com.example.firebase_sinin.network

import com.example.firebase_sinin.BuildConfig

class GeminiApiRepository () {
    suspend fun getAIExplanation(promptText : String): GeminiContentResponse {
        val request = GeminiContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = promptText)
                    )
                )
            )
        )

        val response = GeminiApi.retrofitService.generateContent(
            apiKey = BuildConfig.GEMINI_API_KEY ,
            request = request
        )

        if (!response.isSuccessful) {
            throw Exception("API error: ${response.errorBody()?.string()}")
        }

        return response.body() ?: throw Exception("Empty response body")

    }
}