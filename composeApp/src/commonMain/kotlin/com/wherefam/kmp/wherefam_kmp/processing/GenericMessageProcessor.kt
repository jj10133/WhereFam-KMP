package com.wherefam.kmp.wherefam_kmp.processing

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.e
import com.wherefam.kmp.wherefam_kmp.data.dao.PeerDao
import com.wherefam.kmp.wherefam_kmp.domain.Peer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class GenericMessageProcessor(private val userRepository: UserRepository, private val peerDao: PeerDao): MessageProcessor {
    override suspend fun processMessage(message: String) {
        val individualMessage = message.split("\n").filter { it.isNotBlank() }
        for (msg in individualMessage) {
            try {
                val incomingMessage = Json.decodeFromString<GenericAction>(msg)
                when (incomingMessage.action) {

                    "publicKeyResponse" -> {
                        val publicKey = incomingMessage.data?.jsonObject["publicKey"]?.jsonPrimitive?.content
                        publicKey?.let { key ->
                            userRepository.updatePublicKey(key)
                        }
                            ?: Logger.w("Missing 'value' for action 'requestPublicKey'", tag = "GenericProcessor" )
                    }

                    "locationUpdate" -> {
                        handleLocationUpdate(incomingMessage)
                    }

                    else -> Logger.w("Unknown action: ${incomingMessage.action}", tag = "GenericProcessor")

                }
            } catch (e: Exception) {
                e("Error processing message: ${e.message}", e, tag = "GenericProcessor")
            }
        }
    }

    private suspend fun handleLocationUpdate(incomingMessage: GenericAction) {
        val locationDataJson = incomingMessage.data?.jsonObject
        if (locationDataJson != null) {
            val id = locationDataJson["id"]?.jsonPrimitive?.content
            val name = locationDataJson["name"]?.jsonPrimitive?.content
            val latitude = locationDataJson["latitude"]?.jsonPrimitive?.double
            val longitude = locationDataJson["longitude"]?.jsonPrimitive?.double

            if (id != null && name != null && latitude != null && longitude != null) {
                peerDao.upsert(Peer(id, name, latitude, longitude))
            } else {
                Logger.w("Invalid location update data: Missing required fields.", tag = "GenericProcessor")
            }
        } else {
            Logger.w("Location update message has no data.", tag = "GenericProcessor")
        }
    }

}