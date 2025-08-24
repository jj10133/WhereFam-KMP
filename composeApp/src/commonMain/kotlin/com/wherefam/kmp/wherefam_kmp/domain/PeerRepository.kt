package com.wherefam.kmp.wherefam_kmp.domain

import com.wherefam.kmp.wherefam_kmp.data.dao.PeerDao
import kotlinx.coroutines.flow.Flow

class PeerRepository(private val peerDao: PeerDao) {
    fun getAllPeers(): Flow<List<Peer>> = peerDao.getAllPeers()

    suspend fun upsert(id: String, name: String, latitude: Double, longitude: Double) {
        peerDao.upsert(Peer(id, name, latitude, longitude))
    }

    suspend fun deleteById(id: String) {
        peerDao.deleteById(id)
    }
}