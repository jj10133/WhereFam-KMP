package com.wherefam.kmp.wherefam_kmp.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.wherefam.kmp.wherefam_kmp.database.OnboardingStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingDao {
    @Upsert
    suspend fun upsert(status: OnboardingStatus)

    @Query("SELECT value FROM onboarding_status")
    fun getOnboardingStatus(): Flow<Boolean?>
}