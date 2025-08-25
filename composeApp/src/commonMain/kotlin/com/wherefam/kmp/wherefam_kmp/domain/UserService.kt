package com.wherefam.kmp.wherefam_kmp.domain

import com.wherefam.kmp.wherefam_kmp.processing.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserService() : UserRepository {
    private val _currentPublicKey = MutableStateFlow("")
    override val currentPublicKey: StateFlow<String> = _currentPublicKey.asStateFlow()

    override suspend fun updatePublicKey(key: String) {
        _currentPublicKey.value = key
    }
}