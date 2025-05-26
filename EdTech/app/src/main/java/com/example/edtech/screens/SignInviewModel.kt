package com.example.edtech.screens

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edtech.firebase.AuthResponse
import com.example.edtech.firebase.AuthenticationManager
import com.example.edtech.firebase.getUserData
import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class SignInUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val signInSuccess: Boolean = false,
    val showPassword: Boolean = false
)

data class SignUpUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val signUpSuccess: Boolean = false
)

data class SignInScreenUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val name:String = "",
    val role:String = "",
    val isSignIn: Boolean = true,
    val showPassword: Boolean = false
)

class SignInViewModel(
    private val authenticationManager: AuthenticationManager,
    private val context: Context
) : ViewModel(){
    val _signInUiState = MutableStateFlow(SignInUiState())
    val signInUiState = _signInUiState.asStateFlow()

    val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    val _signInScreenUiState = MutableStateFlow(SignInScreenUiState())
    val signInScreenUiState = _signInScreenUiState.asStateFlow()

    fun updateEmail(newEmail: String) {
        _signInScreenUiState.update { it.copy(email = newEmail) }
    }
    fun updatePassword(newPassword: String) {
        _signInScreenUiState.update { it.copy(password = newPassword) }
    }
    fun updateConfirmPassword(newPassword: String) {
        _signInScreenUiState.update { it.copy(confirmPassword = newPassword) }
    }
    fun updateName(newName: String) {
        _signInScreenUiState.update { it.copy(name = newName) }
    }

    fun resetSignInSuccess() {
        _signInUiState.update { it.copy(signInSuccess = false) }
    }

    fun signIn() {
        _signInUiState.value = _signInUiState.value.copy(isLoading = true)
        authenticationManager.loginWithEmail(email = _signInScreenUiState.value.email, password = _signInScreenUiState.value.password)
            .onEach { response ->
                if (response is AuthResponse.Success ) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        getUserData(uid) { user ->
                            if (user != null && user.role == _signInScreenUiState.value.role) {
                                _signInUiState.update {
                                    it.copy(
                                        signInSuccess = true,
                                        isLoading = false
                                    )
                                }
                            } else {
                                _signInUiState.update {
                                    it.copy(
                                        error = "User role not found",
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    } else {
                        _signInUiState.update {
                            it.copy(
                                error = "User ID is null",
                                isLoading = false
                            )
                        }
                    }
                }
                else {
                    _signInUiState.update {
                        it.copy(
                            error = "unsuccessful login",
                            isLoading = false,
                            signInSuccess = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun register() {
        val password = _signInScreenUiState.value.password
        val confirmPassword = _signInScreenUiState.value.confirmPassword

        if (!confirmPassword(password, confirmPassword)) {
            _signUpUiState.value = SignUpUiState(error = "Passwords do not match")
            return
        }

        _signUpUiState.value = _signUpUiState.value.copy(isLoading = true)

        authenticationManager.createAccountWithEmail(
            email = _signInScreenUiState.value.email,
            password = _signInScreenUiState.value.password ,
            role = _signInScreenUiState.value.role,
            name = _signInScreenUiState.value.name
        )
            .onEach { response ->
                if (response is AuthResponse.Success) {
                    Toast.makeText(context, "Registration successful", Toast.LENGTH_SHORT).show()
                    _signUpUiState.update {
                        it.copy(
                            signUpSuccess = true,
                            isLoading = false
                        )
                    }
                    _signInScreenUiState.update {
                        it.copy(
                            email = "",
                            password = "",
                            confirmPassword = "",
                            name = ""
                        )
                    }
                }
                else {
                    Log.d("SignInViewModel", "unsuccessful registration : ${response.toString()}")
                    _signUpUiState.update {
                        it.copy(
                            error = "unsuccessful registration",
                            isLoading = false,
                            signUpSuccess = false
                        )
                    }
                }
            }.launchIn(viewModelScope)

    }

    fun confirmPassword(password: String , confirmPassword: String): Boolean {
        return (password == confirmPassword)
    }

    fun switchRole(newRole: String) {
        _signInScreenUiState.update {
            it.copy(
                role = newRole
            )
        }
    }

    fun togglePasswordVisibility(){
        _signInScreenUiState.update {
            it.copy(
                showPassword = !it.showPassword
            )
        }
    }

    fun toggleSignInMode(){
        _signInScreenUiState.update {
            it.copy(
                isSignIn = !it.isSignIn
            )
        }
    }

    fun signInWithGoogle() {
        _signInUiState.value = _signInUiState.value.copy(isLoading = true)

        authenticationManager.signInWithGoogle()
            .onEach { response ->
                if (response is AuthResponse.Success) {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    Log.d("SignInViewModel", "User ID: $uid")
                    if (uid != null) {
                        getUserData(uid) { user ->
                            Log.d("SignInViewModel", "User data: $user")
                            if (user != null && user.role == _signInScreenUiState.value.role) {
                                _signInUiState.update {
                                    it.copy(
                                        signInSuccess = true,
                                        isLoading = false
                                    )
                                }
                            } else {
                                _signInUiState.update {
                                    it.copy(
                                        error = "User role not found or mismatched",
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    } else {
                        _signInUiState.update {
                            it.copy(
                                error = "User ID is null",
                                isLoading = false
                            )
                        }
                    }
                } else if (response is AuthResponse.Error) {
                    _signInUiState.update {
                        it.copy(
                            error = response.errorMessage ?: "Google sign-in failed",
                            isLoading = false
                        )
                    }
                }
            }.launchIn(viewModelScope)
    }

}