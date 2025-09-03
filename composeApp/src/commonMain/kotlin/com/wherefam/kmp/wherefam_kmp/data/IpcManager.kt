package com.wherefam.kmp.wherefam_kmp.data

import kotlinx.coroutines.flow.Flow

expect class IpcManager {
    fun getIPC(): Any
    fun setIPC(ipc: Any?)

    suspend fun write(jsonString: String)

//    fun read(): Flow<String>
}