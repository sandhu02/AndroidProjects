package com.example.edtech.screens.teacherScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.edtech.firebase.addCourseToFireStore
import com.example.edtech.firebase.getSignedInUser
import com.example.edtech.model.Course
import kotlinx.coroutines.flow.update


data class AddCourseUiState(
    val title: String = "",
    val description: String = "",
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
        _addCourseUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                addCourseToFireStore(
                    course = courseFactory()
                    //addonsucces and onfailure
                )
                _addCourseUiState.update { it.copy(
                    isLoading = false ,
                    isSuccess = true,
                    title = "",
                    description = "",
                ) }
                Toast.makeText(context , "Course Added", Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception){
                _addCourseUiState.update { it.copy(isLoading = false , isError = "Error adding course ${e.message}") }
            }
        }
    }

    fun courseFactory() : Course {
        return Course(
            title = _addCourseUiState.value.title,
            description = _addCourseUiState.value.description,
            author = getSignedInUser()?.email.toString(),
            rating = 0,
            vidUrl = "",
            imgUrl = ""
        )
    }

}