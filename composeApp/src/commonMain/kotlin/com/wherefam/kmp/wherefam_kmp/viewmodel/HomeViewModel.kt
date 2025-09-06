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

class HomeViewModel(private val ipcManager: IpcManager, peerRepository: PeerRepository) : ViewModel() {

    val peers: StateFlow<List<Peer>> = peerRepository.getAllPeers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )


    fun start(fileDir: String) {
        viewModelScope.launch {
            val dynamicData = buildJsonObject { put("path", fileDir) }
            val message = GenericAction(action = "start", data = dynamicData)

            val jsonString = Json.encodeToString(message) + "\n"
            ipcManager.write(jsonString)
        }
    }

    fun joinAllExistingPeers() {
        viewModelScope.launch {
            for (peer in peers.value) {
                val dynamicData = buildJsonObject { put("peerPublicKey", peer.id) }
                val message = GenericAction(action = "start", data = dynamicData)

                val jsonString = Json.encodeToString(message) + "\n"
                ipcManager.write(jsonString)
            }
        }
    }
}