package com.wherefam.kmp.wherefam_kmp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import okio.Path.Companion.toPath

fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )
}

internal const val DATA_STORE_FILE_NAME = "/datastore.preferences_pb"

class AppSettings(private val datastore: DataStore<Preferences>) {
    private companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    suspend fun updateOnboardingState(completed: Boolean) {
        datastore.edit { preferences ->
            preferences[ONBOARDING_COMPLETED] = completed
        }
    }
}
