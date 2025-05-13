package com.example.rtcaudio

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainScreenUiState(
    val callerIdTextField : String = "",
)

class MainScreenViewModel() : ViewModel() {
    private val _uistate = MutableStateFlow(MainScreenUiState())
    val uiState = _uistate.asStateFlow()

    fun updateCallerIdTextField(newCallerId : String) {
        _uistate.update { it.copy(callerIdTextField = newCallerId) }
    }
}