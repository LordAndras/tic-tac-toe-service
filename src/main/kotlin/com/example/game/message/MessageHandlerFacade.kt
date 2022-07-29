package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class MessageHandlerFacade(
    private val gameMessageService: GameMessageService,
    private val systemMessageService: SystemMessageService,
    private val objectMapper: ObjectMapper
) {
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    fun handleMessage(payload: String): TextMessage {
        val socketMessagePayload = objectMapper.readValue(payload, socketMessageTypeRef)

        return if (socketMessagePayload.system.isEmpty() && socketMessagePayload.gameStateResponse != null) {
            gameMessageService.prepareResponseMessage(socketMessagePayload.gameStateResponse.gameState)
        } else {
            systemMessageService.handleSystemMessage(socketMessagePayload.system)
        }
    }
}