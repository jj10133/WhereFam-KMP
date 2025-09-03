package com.wherefam.kmp.wherefam_kmp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.data.IpcManager
import com.wherefam.kmp.wherefam_kmp.model.Peer
import com.wherefam.kmp.wherefam_kmp.domain.PeerRepository
import com.wherefam.kmp.wherefam_kmp.processing.GenericAction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class PeopleViewModel(private val peerRepository: PeerRepository, private val ipcManager: IpcManager) : ViewModel() {
    val peersList: StateFlow<List<Peer>> = peerRepository.getAllPeers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPeer(id: String, name: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            peerRepository.upsert(id, name, lat, lng)
        }
    }

    fun deletePeer(id: String) {
        viewModelScope.launch {
           peerRepository.deleteById(id)
        }
    }

    fun joinPeer(key: String) {
        viewModelScope.launch {
            val dynamicData = buildJsonObject {
                put("peerPublicKey", key)
            }
            val message = GenericAction(action = "joinPeer", data = dynamicData)
            val jsonString = Json.Default.encodeToString(message) + "\n"
            ipcManager.write(jsonString)
        }
    }

}