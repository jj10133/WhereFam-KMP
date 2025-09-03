package com.wherefam.kmp.wherefam_kmp.data

import kotlinx.coroutines.flow.Flow


actual class IpcManager {
    private var ipc: Any? = null
    actual fun getIPC(): Any {
        return ipc!!
    }

    actual fun setIPC(ipc: Any?) {
        this.ipc = ipc
    }

    actual suspend fun write(jsonString: String) {
        ipcProvider.write(jsonString)
    }
}

interface IPCProvider {
    fun getIPC(): Any
    fun setIPC(ipc: Any?)
    suspend fun write(jsonString: String)
}

lateinit var ipcProvider: IPCProvider