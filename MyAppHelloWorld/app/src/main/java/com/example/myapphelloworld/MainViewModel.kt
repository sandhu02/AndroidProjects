package com.example.myapphelloworld

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MainUiState (
    val counter: Int = 0
)

class MainViewModel () : ViewModel(){
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    fun increaseCount(){
        _uiState.update { it.copy(counter = _uiState.value.counter + 1) }
    }
}