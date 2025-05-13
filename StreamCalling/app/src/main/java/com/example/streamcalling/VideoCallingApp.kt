package com.example.streamcalling

import android.app.Application
import com.example.streamcalling.di.appModule
import io.getstream.video.android.core.StreamVideo
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

const val API_KEY = "rvq6gv8c8wjk"

class VideoCallingApp : Application() {
    private var currentName:String? = null
    var client : StreamVideo? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VideoCallingApp)
            modules(appModule)
        }
    }

    fun initVideoClient(username : String) {
        if (client == null || username != currentName) {
            StreamVideo.removeClient()
            currentName = username

            client = StreamVideoBuilder (
                context = this ,
                apiKey = API_KEY ,
                user = User (
                    id = username ,
                    type = UserType.Guest
                )
            ).build()
        }
    }
}