package com.example.edtech

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.edtech.firebase.AuthenticationManager
import com.example.edtech.screens.SignInViewModel
import com.example.edtech.screens.teacherScreens.AddCourseViewModel
import com.example.edtech.screens.teacherScreens.TeacherChatViewModel
import com.example.edtech.screens.teacherScreens.TeacherCoursesViewModel
import com.example.edtech.screens.teacherScreens.TeacherDashboardViewModel

class EdTechViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    val authenticationManager = AuthenticationManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(authenticationManager , context = context) as T
        }
        else if (modelClass.isAssignableFrom(TeacherDashboardViewModel::class.java)) {
            return TeacherDashboardViewModel(authenticationManager , context = context) as T
        }
        else if (modelClass.isAssignableFrom(TeacherChatViewModel::class.java)) {
            return TeacherChatViewModel() as T
        }
        else if (modelClass.isAssignableFrom(TeacherCoursesViewModel::class.java)) {
            return TeacherCoursesViewModel() as T
        }
        else if(modelClass.isAssignableFrom(AddCourseViewModel::class.java)){
            return AddCourseViewModel(context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}