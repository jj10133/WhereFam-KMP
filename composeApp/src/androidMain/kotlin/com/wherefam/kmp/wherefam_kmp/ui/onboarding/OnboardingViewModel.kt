package com.wherefam.kmp.wherefam_kmp.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.data.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    fun saveOnboardingState(completed: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            dataStoreRepository.saveOnboardingState(completed)
        }
    }
}