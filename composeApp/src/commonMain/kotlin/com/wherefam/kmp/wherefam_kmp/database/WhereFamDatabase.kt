package com.wherefam.kmp.wherefam_kmp.database

import androidx.room.*
import com.wherefam.kmp.wherefam_kmp.database.dao.PeerDao
import com.wherefam.kmp.wherefam_kmp.model.Peer
import kotlinx.datetime.LocalDateTime

internal expect object WhereFamCtor: RoomDatabaseConstructor<WhereFamDatabase>


@Database(entities = [Peer::class], version = 1)
@ConstructedBy(WhereFamCtor::class)
@TypeConverters(LocalDateTimeConverter::class)
abstract class WhereFamDatabase : RoomDatabase() {
    abstract fun peerDao(): PeerDao
}

internal const val dbFileName = "wherefam.db"

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
