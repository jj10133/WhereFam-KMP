package com.wherefam.kmp.wherefam_kmp.ui.onboarding

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.domain.OnboardingRepository
import kotlinx.coroutines.flow.first

import kotlinx.coroutines.launch

class SplashViewModel(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableState<String> = mutableStateOf("Onboarding")
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch {
            val completed = onboardingRepository.getOnboardingStatus().first()
            _startDestination.value = if (completed) "Home" else "Onboarding"
            _isLoading.value = false
        }
    }
}