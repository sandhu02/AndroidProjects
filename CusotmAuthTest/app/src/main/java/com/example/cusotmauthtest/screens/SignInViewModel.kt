package com.example.cusotmauthtest.screens

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.LoginRequest
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import com.example.cusotmauthtest.retrofit.saveToken
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SignInUiState(
    val username: String = "",
    val password: String = "",
    val response: Response? = null,
    val localToken:String? = null
)

class SignInViewModel(val context: Context) : ViewModel(){
    private val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    fun updateUsername(newUsername: String){
        _signInUiState.update { it.copy(username = newUsername) }
    }
    fun updatePassword(newPassword: String){
        _signInUiState.update { it.copy(password = newPassword) }
    }

    fun login() {
        val request = LoginRequest(username = _signInUiState.value.username , password = _signInUiState.value.password)
        viewModelScope.launch {
            _signInUiState.update { it.copy(response = Loading) }
            try{
                val response = getAuthApiInstance(context = context)?.login(request)
                if (response == null){
                    _signInUiState.update { it.copy(response = Failure("Login failed: Empty Response")) }
                }
                else if (response.success){
                    _signInUiState.update {
                        val msg = response.message
                        saveToken(context , response.token)
                        it.copy(
                            response = Success(msg)
                        )
                    }
                }
                else {
                    _signInUiState.update { it.copy(response = Failure("Login failed :${response.message}")) }
                }
            }
            catch (e: Exception){
                _signInUiState.update { it.copy(response = Failure("Network error: ${e.localizedMessage}")) }
            }

        }
    }

    fun updateToken(newToken: String){
        _signInUiState.update { it.copy(localToken = newToken) }
    }
    fun getFirebaseToken() {   //TODO( implement notifications in chat)
        viewModelScope.launch {
            try {
                val token = Firebase.messaging.token.await()
                _signInUiState.update { it.copy(localToken = token) }
                Log.d("FCM", "Token: $token")
            } catch (e: Exception) {
                Log.e("FCM", "Failed to get token", e)
                _signInUiState.update { it.copy(localToken = null) }
            }
        }
    }
}

sealed class Response
data class Success(val message:String): Response()
data class Failure(val message: String): Response()
object Loading: Response()
