package com.wherefam.kmp.wherefam_kmp.ui.home.share

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.wherefam.kmp.wherefam_kmp.data.IPCProvider.ipc
import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.writeAsync
import com.wherefam.kmp.wherefam_kmp.processing.GenericAction
import com.wherefam.kmp.wherefam_kmp.processing.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import to.holepunch.bare.kit.IPC
import java.nio.ByteBuffer
import java.nio.charset.Charset

class ShareViewModel(private val userRepository: UserRepository, private val ipc: IPC) : ViewModel() {
    val publicKey: StateFlow<String> = userRepository.currentPublicKey

    private val _qrCodeBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrCodeBitmap = _qrCodeBitmap.asStateFlow()

    init {
        viewModelScope.launch {
            publicKey.collectLatest { key ->
                if (key.isNotEmpty()) {
                    generateAndSetQrCode(key)
                } else {
                    _qrCodeBitmap.value = null
                }
            }
        }
    }

    suspend fun requestPublicKey() {
        val dynamicData = buildJsonObject {}
        val message = GenericAction(action = "requestPublicKey", data = dynamicData)
        val jsonString = Json.Default.encodeToString(message) + "\n"
        val byteBuffer = ByteBuffer.wrap(jsonString.toByteArray(Charset.forName("UTF-8")))
        ipc.writeAsync(byteBuffer)
    }

    private fun generateAndSetQrCode(shareID: String) {
        viewModelScope.launch {
            val generatedBitmap = withContext(Dispatchers.Default) {
                val size = 512
                val hints = hashMapOf<EncodeHintType, Int>().also {
                    it[EncodeHintType.MARGIN] = 1
                }

                val bits = QRCodeWriter().encode(shareID, BarcodeFormat.QR_CODE, size, size, hints)
                val bitmap = createBitmap(size, size, Bitmap.Config.RGB_565).also {
                    for (x in 0 until size) {
                        for (y in 0 until size) {
                            it[x, y] = if (bits[x, y]) Color.BLACK else Color.WHITE
                        }
                    }
                }

                bitmap.asImageBitmap()
            }

            _qrCodeBitmap.value = generatedBitmap

        }
    }
}