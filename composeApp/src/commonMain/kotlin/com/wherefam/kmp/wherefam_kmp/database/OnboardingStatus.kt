package com.wherefam.kmp.wherefam_kmp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onboarding_status")
data class OnboardingStatus(
    @PrimaryKey val key: String = "onboarding_completed",
    val value: Boolean,
    val username: String
)
