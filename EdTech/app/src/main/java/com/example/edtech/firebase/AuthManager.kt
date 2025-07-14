package com.example.edtech.firebase

import android.util.Log
import com.example.edtech.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.edtech.model.User
import com.google.android.gms.tasks.Task
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager(val context: Context){
    private val auth = Firebase.auth

    private fun createNonce() : String {
        val rawNonce = UUID.randomUUID().toString()
        val byte = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest =  md.digest(byte)

        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun signInWithGoogle() : Flow<AuthResponse> = callbackFlow {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = context, request = request)

            val credential = result.credential
            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)

                        val firebaseCredential = GoogleAuthProvider
                            .getCredential(
                                googleIdTokenCredential.idToken ,
                                null
                            )

                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    trySend(AuthResponse.Success)
                                }
                                else {
                                    trySend(AuthResponse.Error(errorMessage = task.exception?.message?: ""))
                                }
                            }
                    }
                    catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(errorMessage = e.message?: ""))
                    }
                }
            }
        }
        catch (e: Exception) {
            trySend(AuthResponse.Error(errorMessage = e.message?:""))
        }

        awaitClose()
    }

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

fun getSignedInUser(): FirebaseUser?{
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser!=null){
        return currentUser
    }
    else {
        return null
    }
}

sealed interface AuthResponse {
    data object Success : AuthResponse
    data class Error(val errorMessage: String?) : AuthResponse
}