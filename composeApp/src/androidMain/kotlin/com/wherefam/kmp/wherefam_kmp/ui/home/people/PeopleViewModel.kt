package com.wherefam.kmp.wherefam_kmp.ui.home.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wherefam.kmp.wherefam_kmp.data.IPCProvider.ipc
import com.wherefam.kmp.wherefam_kmp.data.IPCUtils.writeAsync
import com.wherefam.kmp.wherefam_kmp.data.dao.PeerDao
import com.wherefam.kmp.wherefam_kmp.domain.Peer
import com.wherefam.kmp.wherefam_kmp.processing.GenericAction
import com.wherefam.kmp.wherefam_kmp.processing.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import to.holepunch.bare.kit.IPC
import java.nio.ByteBuffer
import java.nio.charset.Charset

class PeopleViewModel(private val peerDao: PeerDao, private val ipc: IPC) : ViewModel() {
    private val _peopleList = MutableStateFlow<List<Peer>>(emptyList())
    val peopleList: StateFlow<List<Peer>> = _peopleList

    init {
        viewModelScope.launch {
            peerDao.getAllPeers().collectLatest { peers ->
                _peopleList.value = peers
            }
        }
    }

    fun addPerson(peer: Peer) {
        viewModelScope.launch {
            peerDao.upsert(peer)
        }
    }

    fun removePerson(id: String) {
        viewModelScope.launch {
            val peer = _peopleList.value.find { it.id == id }
            peer?.let { peerDao.delete(it) }
        }
    }

    suspend fun joinPeer(key: String) {
        val dynamicData = buildJsonObject {
            put("peerPublicKey", key)
        }
        val message = GenericAction(action = "joinPeer", data = dynamicData)
        val jsonString = Json.Default.encodeToString(message) + "\n"
        val byteBuffer = ByteBuffer.wrap(jsonString.toByteArray(Charset.forName("UTF-8")))
        ipc.writeAsync(byteBuffer)
    }

}