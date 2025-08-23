package com.wherefam.kmp.wherefam_kmp.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.wherefam.kmp.wherefam_kmp.data.WhereFamDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun getDatabaseBuilder(): RoomDatabase.Builder<WhereFamDatabase> {
    val dbFile = documentDirectory() + "/wherefam.db"
    return Room.databaseBuilder(name = dbFile)
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        NSDocumentDirectory,
        NSUserDomainMask,
        null,
        false,
        null
    )

    return requireNotNull(documentDirectory?.path)
}