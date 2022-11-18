package com.example.game.message

import com.example.game.message.system.SystemMessageService
import com.example.game.model.SocketMessagePayload
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class MessageHandlerFacade(
    private val gameMessageService: GameMessageService,
    private val systemMessageService: SystemMessageService,
    private val objectMapper: ObjectMapper
) {
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    fun handleMessage(session: WebSocketSession, payload: String): TextMessage {
        val socketMessagePayload = objectMapper.readValue(payload, socketMessageTypeRef)

        return if (!socketMessagePayload.isSysMessage && socketMessagePayload.gameStateResponse != null) {
            gameMessageService.prepareResponseMessage(socketMessagePayload.gameStateResponse.gameState)
        } else if (socketMessagePayload.isSysMessage && socketMessagePayload.systemMessage != null) {
            systemMessageService.handleMessage(session, socketMessagePayload.systemMessage)
        } else {
            throw MessagePayloadIsNullException()
        }
    }
}