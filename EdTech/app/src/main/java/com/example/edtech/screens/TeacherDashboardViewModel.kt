package com.example.edtech.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edtech.firebase.AuthenticationManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SignOutUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val signOutSuccess: Boolean = false
)

class TeacherDashboardViewModel (
    private val authenticationManager: AuthenticationManager,
    private val context: Context
) : ViewModel () {

    private val _signOutUiSate = MutableStateFlow(SignOutUiState())
    val signOutUiState = _signOutUiSate.asStateFlow()

    private val _navigateToSignIn = MutableSharedFlow<Unit>()
    val navigateToSignIn: SharedFlow<Unit> = _navigateToSignIn

    fun logout () {
        _signOutUiSate.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            authenticationManager.signOut()

            _signOutUiSate.update { it.copy(
                signOutSuccess = true,
                isLoading = false
            ) }

            _navigateToSignIn.emit(Unit)
        }

    }

}