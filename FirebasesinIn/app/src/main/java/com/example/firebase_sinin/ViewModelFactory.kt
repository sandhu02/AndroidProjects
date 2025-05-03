package com.example.firebase_sinin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebase_sinin.Screens.HomeScreenViewModel
import com.example.firebase_sinin.Screens.SignInViewModel

class SignInViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    val authenticationManager = AuthenticationManager(context)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(authenticationManager) as T
        }
        else if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(authenticationManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}