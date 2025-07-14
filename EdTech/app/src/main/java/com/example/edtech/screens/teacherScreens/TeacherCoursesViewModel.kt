package com.example.edtech.screens.teacherScreens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edtech.firebase.getAllCourses
import com.example.edtech.model.Course
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TeacherCourseUiState(
    val courses: List<Course> = listOf<Course>(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class TeacherCoursesViewModel() : ViewModel() {
    private val _teacherCourseUiState = MutableStateFlow(TeacherCourseUiState())
    val uiState = _teacherCourseUiState.asStateFlow()

    fun loadAllCourses() {
        _teacherCourseUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val onComplete = { courses: List<Course> ->
                    _teacherCourseUiState.update {
                        it.copy(courses = courses , isLoading = false )
                    }
                }
                val onError = {exception: Exception ->
                    _teacherCourseUiState.update {
                        it.copy(error = exception.message , isLoading = false)
                    }
                }
                getAllCourses(
                    onComplete = onComplete,
                    onError = onError,
                )
            }
            catch (e: Exception) {
                Log.e("TeacherCoursesViewModel", "Error loading courses: ${e.message}")
            }
        }
    }

    init {
        loadAllCourses()
    }

}