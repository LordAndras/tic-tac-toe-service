package com.example.game.message.system

import com.example.game.message.MessageUtils
import com.example.game.model.SystemMessage
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class SystemMessageService(
    private val systemMessageHandlerProvider: SystemMessageHandlerProvider
) {
    fun prepareResponseMessage(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        return try {
            val handler = systemMessageHandlerProvider.provide(systemMessage)
            handler.handle(session, systemMessage)
        } catch (exception: Exception) {
            TextMessage(MessageUtils.createErrorPayload(exception.message ?: "Error occurred"))
        }
    }
}