package com.wherefam.kmp.wherefam_kmp.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wherefam.kmp.wherefam_kmp.data.WhereFamDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<WhereFamDatabase> {
    val dbFile = context.getDatabasePath("wherefam.db")
    return Room.databaseBuilder(
        context,
        dbFile.absolutePath
    )
}