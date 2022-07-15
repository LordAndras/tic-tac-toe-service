package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class MessageHandlerFacade(
    private val messageHandlerService: MessageHandlerService,
    private val objectMapper: ObjectMapper
) {
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    fun handleMessage(message: String): TextMessage {
        val payload = objectMapper.readValue(message, socketMessageTypeRef)

        return if (payload.system.isEmpty() && payload.gameStateResponse != null) {
            messageHandlerService.prepareResponseMessage(payload.gameStateResponse.gameState)
        } else {
            TextMessage("")
        }
    }
}