package com.example.edtech.screens.teacherScreens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.edtech.firebase.addCourseToFireStore
import com.example.edtech.firebase.getSignedInUser
import com.example.edtech.firebase.uploadVideoToFirebase
import com.example.edtech.model.Course
import kotlinx.coroutines.flow.update


data class AddCourseUiState(
    val title: String = "",
    val description: String = "",
    val videoUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = null
)

class AddCourseViewModel (val context: Context) : ViewModel() {
    private val _addCourseUiState = MutableStateFlow(AddCourseUiState())
    val addCourseUiState = _addCourseUiState.asStateFlow()

    fun updateTitle(title: String) {
        _addCourseUiState.update { it.copy(title = title) }
    }
    fun updateDescription(description: String) {
        _addCourseUiState.update { it.copy(description = description) }
    }

    fun addCourse() {
        val uri = _addCourseUiState.value.videoUri
        if (uri == null) {
            Toast.makeText(context, "Please select a video first", Toast.LENGTH_SHORT).show()
            return
        }
        _addCourseUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                uploadVideoToFirebase(
                    videoUri = uri ,
                    onSuccess = { videoUrl ->
                        val course = courseFactory(videoUrl)
                        addCourseToFireStore(course)

                        Toast.makeText(context , "Course Added", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { exception:Exception ->
                        Toast.makeText(context , "Failed to Add Course ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
                _addCourseUiState.update { it.copy(
                    isLoading = false ,
                    isSuccess = true,
                    title = "",
                    description = "",
                ) }
            }
            catch (e: Exception){
                _addCourseUiState.update { it.copy(isLoading = false , isError = "Error adding course ${e.message}") }
            }
        }
    }

    fun courseFactory(vidUrl: String) : Course {
        return Course(
            title = _addCourseUiState.value.title,
            description = _addCourseUiState.value.description,
            author = getSignedInUser()?.email.toString(),
            rating = 0,
            vidUrl = vidUrl,
            imgUrl = ""
        )
    }

//    fun getVideoUri(){
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
//        intent.type = "video/*"
//        startActivityForResult(intent, VIDEO_PICK_CODE)
//    }

    fun setVideoUri(uri: Uri) {
        _addCourseUiState.update { it.copy(videoUri = uri) }
    }

}