package com.wherefam.kmp.wherefam_kmp.processing

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Logger.Companion.e
import kotlinx.serialization.json.Json

class GenericMessageProcessor: MessageProcessor {
    override suspend fun processMessage(message: String) {
        val individualMessage = message.split("\n").filter { it.isNotBlank() }
        for (msg in individualMessage) {
            try {
//                val incomingMessage = Json.decodeFromString<GenericAction>(msg)
//                when (incomingMessage.action) {

                     Logger.w(msg, tag = "GenericProcessor")

//                }
            } catch (e: Exception) {
                Logger.e("Error processing message: ${e.message}", e, tag = "GenericProcessor")
            }
        }
    }

}