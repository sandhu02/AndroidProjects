package com.example.edtech

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.edtech.ui.theme.EdTechTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savetofirestore()
        enableEdgeToEdge()
        setContent {
            EdTechTheme {
                EdTechApp()
            }
        }
    }
}

class EdTechApplication : Application () {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: EdTechApplication
            private set
    }
}