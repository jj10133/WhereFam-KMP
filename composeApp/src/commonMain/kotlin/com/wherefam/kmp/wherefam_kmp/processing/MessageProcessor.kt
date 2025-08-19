package com.wherefam.kmp.wherefam_kmp.processing

interface MessageProcessor {
    suspend fun processMessage(message: String)
}