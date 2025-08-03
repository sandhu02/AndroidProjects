package com.example.cusotmauthtest.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.LoginRequest
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInUiState(
    val username: String = "",
    val password: String = "",
    val response: Response? = null
)

class SignInViewModel() : ViewModel(){
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
                val response = getAuthApiInstance()?.login(request)
                if (response == null){
                    _signInUiState.update { it.copy(response = Failure("Login failed: Empty Response")) }
                }
                else if (response.success){
                    _signInUiState.update {
                        val msg = response.message
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
}

sealed class Response
data class Success(val message:String): Response()
data class Failure(val message: String): Response()
object Loading: Response()
