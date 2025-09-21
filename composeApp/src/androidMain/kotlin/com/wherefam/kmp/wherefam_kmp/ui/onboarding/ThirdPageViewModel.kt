package com.wherefam.kmp.wherefam_kmp.ui.onboarding

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class ThirdPageViewModel(private val context: Context) : ViewModel() {

    private val contentResolver = context.contentResolver
    var userImage: ImageBitmap? by mutableStateOf(null)
        private set

    fun loadImageFromUri(uri: Uri?) {
        if (uri == null) {
            userImage = null
            viewModelScope.launch { saveUserImage(null) }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap: Bitmap? = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))

                withContext(Dispatchers.Main) {
                    userImage = bitmap?.asImageBitmap()
                    Log.d("ThirdPageViewModel", "Image loaded from URI and saved")
                }

                saveUserImage(bitmap)
            } catch (e: Exception) {
                Log.e("ThirdPageViewModel", "Error loading image from URI: ${e.message}", e)
                userImage = null
                viewModelScope.launch { saveUserImage(null) }
            }
        }
    }

    private suspend fun saveUserImage(bitmap: Bitmap?) {
        withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, "user_image.jpg")
                if (bitmap != null) {
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                    }
                    Log.d("PrefUtils", "Image saved to internal storage: ${file.absolutePath}")
                }
            } catch (e: Exception) {
                Log.e("PrefUtils", "Error saving image to internal storage: ${e.message}", e)
            }
        }
    }
}