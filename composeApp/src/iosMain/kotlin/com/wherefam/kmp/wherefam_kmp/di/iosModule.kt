package com.wherefam.kmp.wherefam_kmp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wherefam.kmp.wherefam_kmp.DATA_STORE_FILE_NAME
import com.wherefam.kmp.wherefam_kmp.createDataStore
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

val iosModule = module {
    single { dataStore() }
}

@OptIn(ExperimentalForeignApi::class)
fun dataStore(): DataStore<Preferences> {
    return createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"

    }
}