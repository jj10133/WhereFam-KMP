package com.wherefam.kmp.wherefam_kmp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.wherefam.kmp.wherefam_kmp.domain.Peer
import kotlinx.coroutines.flow.Flow

@Dao
interface PeerDao {
    @Upsert
    suspend fun upsert(peer: Peer)

    @Delete
    suspend fun delete(peer: Peer)

    @Transaction
    @Query("SELECT * FROM peer")
    fun getAllPeers(): Flow<List<Peer>>
}