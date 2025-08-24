package com.wherefam.kmp.wherefam_kmp.ui.home.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.writeAsync
import com.wherefam.kmp.wherefam_kmp.domain.Peer
import com.wherefam.kmp.wherefam_kmp.domain.PeerRepository
import com.wherefam.kmp.wherefam_kmp.processing.GenericAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import to.holepunch.bare.kit.IPC
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PeopleViewModel(private val peerRepository: PeerRepository, private val ipc: IPC) : ViewModel() {
    val peersList: Flow<List<Peer>> = peerRepository.getAllPeers()

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
            val byteBuffer = ByteBuffer.wrap(jsonString.toByteArray(Charset.forName("UTF-8")))
            ipc.writeAsync(byteBuffer)
        }
    }

}