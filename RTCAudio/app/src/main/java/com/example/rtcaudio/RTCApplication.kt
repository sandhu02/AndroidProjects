package com.example.rtcaudio

import android.app.Application

class RTCApplication : Application () {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: RTCApplication
            private set
    }
}