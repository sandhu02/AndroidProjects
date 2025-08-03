package com.example.cusotmauthtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cusotmauthtest.screens.RegisterViewModel
import com.example.cusotmauthtest.screens.SignInViewModel

class AuthTestViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)){
            return SignInViewModel() as T
        }
        else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}