package com.example.streamcalling.di

import com.example.streamcalling.Video.VideoCallViewModel
import com.example.streamcalling.VideoCallingApp
import com.example.streamcalling.connect.ConnectViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    factory {
        val app = androidContext().applicationContext as VideoCallingApp
        app.client
    }

    viewModelOf(::ConnectViewModel)
    viewModelOf(::VideoCallViewModel)
}