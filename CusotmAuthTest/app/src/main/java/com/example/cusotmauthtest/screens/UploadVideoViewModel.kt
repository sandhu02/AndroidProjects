package com.example.cusotmauthtest.screens

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cusotmauthtest.retrofit.getAuthApiInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

data class UploadVideoUiState(
    val title: String = "",
    val description: String = "",
    val videoUri: Uri? = null,
    val response: UploadVideoResponse? = null,
    val showingDialog: Boolean = false
)

class UploadVideoViewModel () : ViewModel() {
    private val _uiState = MutableStateFlow(UploadVideoUiState())
    val uiState = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }
    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }
    fun updateUri(uri: Uri?) {
        _uiState.update { it.copy(videoUri = uri) }
    }

    fun uploadVideo(context: Context){
        if (_uiState.value.videoUri != null){
            val file = uriToFile(context = context, uri = _uiState.value.videoUri!! , videoName = _uiState.value.title) // path from content resolver or camera
            val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())

            val videoPart = MultipartBody.Part.createFormData("video", file.name, requestFile)
            val titlePart = (_uiState.value.title).toRequestBody("text/plain".toMediaTypeOrNull())
            val descPart = (_uiState.value.description).toRequestBody("text/plain".toMediaTypeOrNull())

            viewModelScope.launch {
                _uiState.update { it.copy(response = UploadVideoResponse.Loading , showingDialog = true) }
                try {
                    val response = getAuthApiInstance(context)?.uploadVideo(videoPart,titlePart,descPart)
                    if (response == null){
                        _uiState.update { it.copy(response = UploadVideoResponse.Failure("Upload failed:Empty Response") , showingDialog = true) }
                    }
                    else if (response.success ) {
                        _uiState.update { it.copy(response = UploadVideoResponse.Success(response.message) , showingDialog = true) }
                    } else {
                        _uiState.update { it.copy(response = UploadVideoResponse.Failure(response.message) , showingDialog = true) }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(response = UploadVideoResponse.Failure(e.message ?: "Error") , showingDialog = true) }
                }
            }
        }
        else {
            _uiState.update { it.copy(response = UploadVideoResponse.Failure("Please select a video before uploading") , showingDialog = true) }
        }
    }

    private fun uriToFile(context: Context, uri: Uri , videoName: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "${videoName}.mp4")
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    fun closeDialog(){
        _uiState.update { it.copy(showingDialog = false) }
    }

    fun resetFields(){
        _uiState.update { it.copy(title = "" , description = "" , videoUri = null) }
    }
}


sealed class UploadVideoResponse() {
    data class Success(val message: String) : UploadVideoResponse()
    data class Failure(val error : String) : UploadVideoResponse()
    object Loading : UploadVideoResponse()
}