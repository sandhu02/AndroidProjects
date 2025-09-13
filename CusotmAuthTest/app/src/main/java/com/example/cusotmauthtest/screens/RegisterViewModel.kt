package com.example.cusotmauthtest.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.RegisterRequest
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val username: String = "",
    val name: String = "",
    val password: String = "",
    val response: Response? = null
)

class RegisterViewModel(val context: Context) : ViewModel() {
    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState = _registerUiState.asStateFlow()

    fun updateUsername(newUsername: String){
        _registerUiState.update { it.copy(username = newUsername) }
    }
    fun updateName(newName: String){
        _registerUiState.update { it.copy(name = newName) }
    }
    fun updatePassword(newPassword: String){
        _registerUiState.update { it.copy(password = newPassword) }
    }

    fun register() {
        val registerRequest = RegisterRequest(
            username = _registerUiState.value.username,
            name = _registerUiState.value.name,
            password = _registerUiState.value.name,
        )
        viewModelScope.launch {
            _registerUiState.update { it.copy(response = Loading) }
            try {
                val response = getAuthApiInstance(context = context)?.register(registerRequest)
                if (response == null) {
                    _registerUiState.update { it.copy(response = Failure("Empty Response Body")) }
                }
                else if (response.success){
                    _registerUiState.update { it.copy(
                        response = Success(response.message),
                    ) }
                }
                else {
                    _registerUiState.update { it.copy(response = Failure("Register Failed: ${response.message}")) }
                }
            }
            catch (e: Exception){
                _registerUiState.update { it.copy(response = Failure("Network error: ${e.localizedMessage}")) }
            }
        }

    }
}