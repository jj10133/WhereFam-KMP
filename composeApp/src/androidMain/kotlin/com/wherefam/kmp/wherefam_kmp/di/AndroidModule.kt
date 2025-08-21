package com.wherefam.kmp.wherefam_kmp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wherefam.kmp.wherefam_kmp.DATA_STORE_FILE_NAME
import com.wherefam.kmp.wherefam_kmp.createDataStore
import org.koin.dsl.module

val androidModule = module {
    single { dataStore(get()) }
}

fun dataStore(context: Context): DataStore<Preferences> {
    return createDataStore {
        context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
    }
}