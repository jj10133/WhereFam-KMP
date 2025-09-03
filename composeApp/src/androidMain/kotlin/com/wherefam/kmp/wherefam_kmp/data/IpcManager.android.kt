package com.wherefam.kmp.wherefam_kmp.data

import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.readStream
import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.writeStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import to.holepunch.bare.kit.IPC
import java.nio.ByteBuffer
import java.nio.charset.Charset

actual class IpcManager {
    private var ipc: IPC? = null
    actual fun getIPC(): Any {
        return ipc as IPC
    }

    actual fun setIPC(ipc: Any?) {
        this.ipc = ipc as? IPC
    }

    actual suspend fun write(jsonString: String) {
        val byteBuffer = ByteBuffer.wrap(jsonString.toByteArray(Charset.forName("UTF-8")))
        ipc?.writeStream(byteBuffer)
    }

    fun read(): Flow<String> {
        return ipc?.readStream()?.map { byteBuffer ->
            val byteArray = ByteArray(byteBuffer.remaining())
            byteBuffer.get(byteArray)
            String(byteArray, Charset.forName("UTF-8"))
        } ?: flowOf()
    }
}