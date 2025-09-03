package com.wherefam.kmp.wherefam_kmp.domain

import com.wherefam.kmp.wherefam_kmp.database.WhereFamDatabase
import com.wherefam.kmp.wherefam_kmp.model.Peer
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PeerRepository() : KoinComponent {
    private val database: WhereFamDatabase by inject()
    fun getAllPeers(): Flow<List<Peer>> = database.peerDao().getAllPeers()

    suspend fun upsert(id: String, name: String, latitude: Double, longitude: Double) {
        database.peerDao().upsert(Peer(id, name, latitude, longitude))
    }

    suspend fun deleteById(id: String) {
        database.peerDao().deleteById(id)
    }
}