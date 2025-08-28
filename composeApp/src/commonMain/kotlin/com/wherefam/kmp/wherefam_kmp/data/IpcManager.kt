package com.wherefam.kmp.wherefam_kmp.data

expect class IpcManager {
    fun getIPC(): Any
    fun setIPC(ipc: Any?)
}