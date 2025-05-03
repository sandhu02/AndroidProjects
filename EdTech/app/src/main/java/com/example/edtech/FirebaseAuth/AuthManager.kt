package com.example.edtech.FirebaseAuth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthenticationManager(){
    private val auth = Firebase.auth

    fun createAccountWithEmail(email: String, password: String , role:String , name:String): Flow<AuthResponse> = callbackFlow {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result.user?.uid
                    if (userId!=null){
                        saveUserRoleToFirestore(userId, role , name = name).addOnCompleteListener { saveTask ->
                            if (saveTask.isSuccessful) {
                                trySend(AuthResponse.Success).isSuccess
                            }
                            else {
                                Log.e("AuthenticationManager", "Failed to save user role", saveTask.exception)
                                trySend(AuthResponse.Error("Failed to save user role"))
                            }
                        }
                    }
                    else {
                        Log.e("AuthenticationManager", "user id is null" )
                        trySend(AuthResponse.Error("User ID is null"))
                    }

                }
                else {
                    Log.e("AuthenticationManager", "task is not successful" )
                    val exceptionMessage = task.exception?.message ?: "Unknown error"
                    Log.e("AuthenticationManager", "Account creation failed: $exceptionMessage", task.exception)
                    trySend(AuthResponse.Error(errorMessage = task.exception?.message ?: "")).isFailure
                }
            }

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