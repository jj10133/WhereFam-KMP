package com.wherefam.kmp.wherefam_kmp.data

import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.readStream
import com.wherefam.kmp.wherefam_kmp.processing.MessageProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import to.holepunch.bare.kit.IPC
import java.nio.charset.Charset

class IPCMessageConsumer(
    private val ipc: IPC,
    private val messageProcessor: MessageProcessor
) {
    var lifecycleScope: LifecycleCoroutineScope? = null

    fun startConsuming() {
        lifecycleScope?.launch(Dispatchers.IO) {
            ipc.readStream().collectLatest { buffer ->
                val utf8Charset = Charset.forName("UTF-8")
                val message = utf8Charset.decode(buffer).toString()

                messageProcessor.processMessage(message)
            }
        } ?: Log.e("IPCMessageConsumer", "lifecycleScope is null, cannot start consuming.")
    }
}