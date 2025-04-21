package com.example.marsimage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsimage.data.MarsPhoto
import com.example.marsimage.data.MarsPhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MarsUiState (
    val photos: List<MarsPhoto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MarsViewModel: ViewModel(){
    private val marsrepository = MarsPhotoRepository()

    private val _uiState = MutableStateFlow(MarsUiState())
    val uiState : StateFlow<MarsUiState> = _uiState.asStateFlow()


    fun fetchPhotos() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            try {
                val photos = marsrepository.getPhotos()
                _uiState.update { currentState ->
                    currentState.copy(
                        photos = photos,
                        isLoading = false,
                        error = null
                    )
                }
            }
            catch (e:Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "some error occured , ${e.message}"
                    )
                }
            }
        }

    }

    init {
        fetchPhotos()
    }
}