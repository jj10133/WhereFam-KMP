package com.wherefam.kmp.wherefam_kmp.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wherefam.kmp.wherefam_kmp.data.IpcManager
import com.wherefam.kmp.wherefam_kmp.database.WhereFamDatabase
import com.wherefam.kmp.wherefam_kmp.database.dbFileName
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

actual fun platformModule() = module {
    single<WhereFamDatabase> { createRoomDatabase(get()) }
    single { IpcManager() }
}

fun createRoomDatabase(ctx: Context): WhereFamDatabase {
    val dbFile = ctx.getDatabasePath(dbFileName)
    return Room.databaseBuilder<WhereFamDatabase>(ctx, dbFile.absolutePath)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}