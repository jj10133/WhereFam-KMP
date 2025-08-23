package com.wherefam.kmp.wherefam_kmp.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.wherefam.kmp.wherefam_kmp.data.dao.PeerDao
import com.wherefam.kmp.wherefam_kmp.domain.Peer

@Database(
    entities = [Peer::class],
    version = 1,
    exportSchema = true
)

@ConstructedBy(WhereFamDatabaseConstructor::class)
abstract class WhereFamDatabase : RoomDatabase() {
    abstract fun peerDao(): PeerDao
}

@Suppress("KotlinNoActualForExpect")
expect object WhereFamDatabaseConstructor: RoomDatabaseConstructor<WhereFamDatabase> {
    override fun initialize(): WhereFamDatabase
}
