package com.wherefam.kmp.wherefam_kmp.data

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import to.holepunch.bare.kit.IPC
import java.nio.ByteBuffer
import kotlin.coroutines.resume

object IPCUtils {
    fun IPC.readStream(): Flow<ByteBuffer> = callbackFlow {
        val readableCallback = IPC.PollCallback {
            try {
                var buffer: ByteBuffer?
                while (true) {
                    buffer = this@readStream.read()
                    if (buffer == null) break
                    trySend(buffer).isSuccess
                }
            } catch (e: Exception) {
                close(e)
            }
        }

        readable(readableCallback)

        awaitClose {
            readable(null)
        }
    }

    suspend fun IPC.writeStream(data: ByteBuffer): Boolean = suspendCancellableCoroutine { continuation ->
        val written = this.write(data)
        if (written == data.limit()) {
            continuation.resume(true)
            return@suspendCancellableCoroutine
        }

        val writableCallback = IPC.PollCallback {
            val writtenAgain = this.write(data)
            if (writtenAgain == data.limit()) {
                this.writable(null)
                continuation.resume(true)
            }
        }

        this.writable(writableCallback)

        continuation.invokeOnCancellation {
            this.writable(null)
        }
    }
}

