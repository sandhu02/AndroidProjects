package com.example.kotlintest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val generating: Boolean = false,
    val randomNumber:Int = -1
)

class MainViewModel() : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private var collectionJob:Job? = null

    fun getNumber(){
        _mainUiState.update { it.copy(generating = true) }
        val numberCallObj = NumberCall()

        collectionJob = viewModelScope.launch {
            val flow = numberCallObj.numberCall()
            flow.collect {  num ->
                _mainUiState.update { it.copy(randomNumber = num) }
            }
        }
    }

    fun cancelGeneration(){
        collectionJob?.cancel()
        _mainUiState.update { it.copy(generating = false) }
    }
}