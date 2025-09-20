package com.wherefam.kmp.wherefam_kmp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.domain.OnboardingRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val onboardingRepository: OnboardingRepository,
) : ViewModel() {

    fun saveOnboardingState(completed: Boolean) {
        viewModelScope.launch {
            onboardingRepository.upsert(completed)
        }
    }
}