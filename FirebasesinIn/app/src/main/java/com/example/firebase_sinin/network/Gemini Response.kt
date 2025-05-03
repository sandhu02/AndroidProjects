package com.example.firebase_sinin.network

data class GeminiContentResponse (
    var candidates    : ArrayList<Candidate> = arrayListOf()
){
    fun extractText(): String {
        return candidates
            .firstOrNull()
            ?.content
            ?.parts
            ?.joinToString("\n") { it.text }
            ?: "No content available"
    }
}

data class Candidate(
    val content: Content?
)

