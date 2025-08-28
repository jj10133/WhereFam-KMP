package com.wherefam.kmp.wherefam_kmp.data

import co.touchlab.kermit.Logger

actual class IpcManager {
    private var ipc: Any? = null
    actual fun getIPC(): Any {
        return ipc!!
    }

    fun setIPC(ipc: Any?) {
        this.ipc = ipc
        Logger.e { "IPC set successfully called" }
    }
}