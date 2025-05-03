package com.example.cameratest

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream

class CameraPreviewViewmodel : ViewModel(){
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()

    fun onTakePhoto(bitmap : Bitmap) {
        _bitmaps.value += bitmap
    }

    private fun saveBitmapToCache(context: Context, bitmap: ImageBitmap): File {
        val cachePath = File(context.cacheDir, "images").apply { mkdirs() }
        val file = File(cachePath, "shared_image.jpg")
        val stream = FileOutputStream(file)
        (bitmap.asAndroidBitmap()).compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
        return file
    }

    fun shareImage(context: Context , bitmap: ImageBitmap){
        val imagefile = saveBitmapToCache(context , bitmap)

        val uri = FileProvider.getUriForFile(
            context ,
            "${context.packageName}.provider",
            imagefile
        )

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Image via"))
    }

    fun saveImagetoStorage(context: Context , bitmap : Bitmap , fileName : String) {
        val directory = File(context.filesDir, "photos").apply { mkdirs() }

        val file = File(directory, "$fileName.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }
}