package com.example.game.message.system

import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class SystemMessageService(
    private val systemMessageHandlerProvider: SystemMessageHandlerProvider,
    private val objectMapper: ObjectMapper
) {
    fun prepareResponseMessage(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        return try {
            val handler = systemMessageHandlerProvider.provide(systemMessage)
            handler.handle(session, systemMessage)
        } catch (exception: Exception) {
            TextMessage(objectMapper.writeValueAsString(createErrorPayload(exception.message ?: "Error occurred")))
        }
    }

    private fun createErrorPayload(message: String): SocketMessagePayload {
        val errorSystemMessage = SystemMessage("error", message)
        return SocketMessagePayload(isSysMessage = true, systemMessage = errorSystemMessage)
    }
}