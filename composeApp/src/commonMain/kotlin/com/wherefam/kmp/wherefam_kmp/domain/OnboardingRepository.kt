package com.wherefam.kmp.wherefam_kmp.domain

import com.wherefam.kmp.wherefam_kmp.database.OnboardingStatus
import com.wherefam.kmp.wherefam_kmp.database.WhereFamDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnboardingRepository: KoinComponent {
    private val database: WhereFamDatabase by inject()

    suspend fun upsert(completed: Boolean, username: String) {
        database.onboardingDao().upsert(OnboardingStatus(value = completed, username = username))
    }

    fun getOnboardingStatus(): Flow<Boolean> {
        return database.onboardingDao().getOnboardingStatus().map { status ->
            status ?: false
        }
    }

    fun getUserName(): Flow<String> {
        return database.onboardingDao().getUserName()
            .map { it ?: "" }
    }
}