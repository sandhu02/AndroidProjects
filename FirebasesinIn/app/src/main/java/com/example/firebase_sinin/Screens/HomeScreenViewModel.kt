package com.example.firebase_sinin.Screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebase_sinin.AuthenticationManager
import com.example.firebase_sinin.network.GeminiApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    var error: String? = null,
    val promptField:String = "" ,
    val responseField:String = ""
)

class HomeScreenViewModel(private val authenticationManager: AuthenticationManager) : ViewModel(){
    val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    val _signoutState = MutableStateFlow(SignInState())
    val signoutState = _signoutState.asStateFlow()

    private val repository = GeminiApiRepository()

    fun updatePromptField(prompt:String){
        _uiState.update { it.copy(promptField = prompt) }
    }


    fun generateAiResponse(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = repository.getAIExplanation(promptText = _uiState.value.promptField)
                _uiState.update {
                    it.copy(isLoading = false , responseField = result.extractText())
                }
                Log.d("HomeScreenViewModel", "generateAiResponse: ${result.candidates.toString()}")
            }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false , responseField = "some error caught in viewmodel ${e.message}" ) }
            }
        }
    }

    fun resetSignOutSuccess() {
        _signoutState.update { it.copy(isSignOutSuccessful = false) }
    }

    fun SignOut() {
        _signoutState.value = _signoutState.value.copy(isLoading = true)
        viewModelScope.launch {
            authenticationManager.signOut()
            _signoutState.update {
                it.copy(
                    Success = "Logged out successfully",
                    isSignOutSuccessful = true,
                    isLoading = false
                )
            }
        }
    }

    fun shareAiText(context: Context) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, _uiState.value.responseField)
        }

        context.startActivity(
            Intent.createChooser(intent , "Share with")
        )
    }
}