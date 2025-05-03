package com.example.firebase_sinin.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_sinin.AuthResponse
import com.example.firebase_sinin.AuthenticationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignInState(
    val email: String = "",
    val password: String = "",
    var Success : String = "",
    val isLoading: Boolean = false,
    var error: String? = null,
    val isSignInSuccessful:Boolean = false,
    val isSignOutSuccessful:Boolean = false
)

class SignInViewModel(private val authenticationManager: AuthenticationManager) : ViewModel() {
    val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun updatePassword(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun resetSignInSuccess() {
        _uiState.update { it.copy(isSignInSuccessful = false) }
    }

    fun SignIn() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        authenticationManager.loginWithEmail(email = _uiState.value.email , password = _uiState.value.password)
            .onEach { response ->
                if (response is AuthResponse.Success) {
                    _uiState.update {
                        it.copy(
                            Success = "Logged In successfully",
                            isSignInSuccessful = true,
                            isLoading = false,
                        )
                    }
                }
                else{
                    _uiState.update {
                        it.copy(
                            error = "unsuccessful login",
                            isLoading = false
                        )
                    }
                }
            }.launchIn(viewModelScope)

    }

    fun register() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        authenticationManager.createAccountWithEmail(email = _uiState.value.email , password = _uiState.value.password)
            .onEach { response ->
                if (response is AuthResponse.Success) {
                    _uiState.update {
                        it.copy(
                            Success = "Registered Successfully",
                            isLoading = false
                        )
                    }
                }
                else{
                    _uiState.update {
                        it.copy(
                            error = "Error occured while registering",
                            isLoading = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

}