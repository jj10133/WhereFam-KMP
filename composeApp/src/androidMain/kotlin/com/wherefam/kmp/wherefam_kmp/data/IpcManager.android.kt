package com.wherefam.kmp.wherefam_kmp.data

import to.holepunch.bare.kit.IPC

actual class IpcManager {
    private var ipc: IPC? = null
    actual fun getIPC(): Any {
        return ipc!!
    }

    actual fun setIPC(ipc: Any?) {
        this.ipc = ipc as? IPC
    }
}