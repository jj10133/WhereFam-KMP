package com.wherefam.kmp.wherefam_kmp.processing

import kotlinx.coroutines.flow.StateFlow


interface UserRepository {
    val currentPublicKey: StateFlow<String>

    suspend fun updatePublicKey(key: String)
}