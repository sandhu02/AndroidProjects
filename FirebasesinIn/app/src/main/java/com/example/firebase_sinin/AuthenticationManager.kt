package com.example.firebase_sinin

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager (
    private val context: Context
){
    private val auth = Firebase.auth

    private fun createNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)

        return digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
    }

    fun createAccountWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Account creation success
                    trySend(AuthResponse.Success).isSuccess
                } else {
                    // Account creation failed
                    trySend(AuthResponse.Error(errorMessage = task.exception?.message)).isFailure
                }
            }

        // Make sure to close the flow otherwise it will crash
        awaitClose()
    }

    fun loginWithEmail(email: String, password: String): Flow<AuthResponse> = callbackFlow {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    trySend(AuthResponse.Success).isSuccess
                } else {
                    // Sign in failed
                    trySend(AuthResponse.Error(errorMessage = task.exception?.message)).isFailure
                }
            }

        // Make sure to close the flow otherwise it will crash
        awaitClose()
    }

    fun signOut() {
        auth.signOut()
    }

}

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val errorMessage: String?) : AuthResponse
}