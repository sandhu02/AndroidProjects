package com.example.cusotmauthtest.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SplashScreenUiState(
    val response: SplashResponse? = null
)

class SplashViewModel (val context: Context) : ViewModel () {
    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun verifyToken() {
        viewModelScope.launch {
            _uiState.update { it.copy(response = SplashResponse.Loading) }
            try {
                val response = getAuthApiInstance(context = context)?.verifyToken()

                if (response == null){
                    _uiState.update { it.copy(response = SplashResponse.Failure("Empty Response")) }
                }
                else if (!response.success) {
                    _uiState.update { it.copy(response = SplashResponse.Failure(response.message)) }
                }
                else {
                    _uiState.update { it.copy(response = SplashResponse.Success(response.message) ) }
                }
            }
            catch(e: Exception){
                _uiState.update { it.copy(response = SplashResponse.Failure("Network Error: ${e.localizedMessage}"))  }
            }
        }
    }

    init {
        verifyToken()
    }
}

sealed class SplashResponse() {
    data class Success(val message: String) : SplashResponse()
    data class Failure(val message: String) : SplashResponse()
    object Loading : SplashResponse()
}