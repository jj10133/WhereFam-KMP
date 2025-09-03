package com.wherefam.kmp.wherefam_kmp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.wherefam.kmp.wherefam_kmp.model.Peer
import kotlinx.coroutines.flow.Flow

@Dao
interface PeerDao {
    @Upsert
    suspend fun upsert(peer: Peer)

    @Query("DELETE FROM peer WHERE id = :id")
    suspend fun deleteById(id: String)

    @Transaction
    @Query("SELECT * FROM peer")
    fun getAllPeers(): Flow<List<Peer>>
}