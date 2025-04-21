package com.example.marsimage.data

import android.util.Log
import com.example.marsimage.network.MarsApi

class MarsPhotoRepository() {
    suspend fun getPhotos():List<MarsPhoto> {
        return MarsApi.retrofitService.getPhotos()
    }
}