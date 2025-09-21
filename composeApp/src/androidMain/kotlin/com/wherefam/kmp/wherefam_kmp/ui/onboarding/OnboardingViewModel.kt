package com.wherefam.kmp.wherefam_kmp.ui.onboarding

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.domain.OnboardingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    val storedUsername: StateFlow<String> = onboardingRepository
        .getUserName()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    // Temp input before save
    private val _usernameInput = mutableStateOf("")
    val usernameInput: State<String> get() = _usernameInput

    fun updateUserName(userName: String) {
        _usernameInput.value = userName
    }

    fun saveOnboardingState(completed: Boolean) {
        viewModelScope.launch {
            onboardingRepository.upsert(completed, _usernameInput.value)
        }
    }
}