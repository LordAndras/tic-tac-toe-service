package com.example.game.message.system.handler

import com.example.game.message.system.SystemMessageHandler
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class InviteHandler(private val sessionHandler: SessionHandler, private val objectMapper: ObjectMapper) :
    SystemMessageHandler {
    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        return try {
            sessionHandler.createGameSession(session, systemMessage)
            TextMessage(createSuccessPayload())
        } catch (exception: Exception) {
            TextMessage(createErrorPayload(exception.message ?: "Error occurred"))
        }
    }

    private fun createSuccessPayload(): String {
        val successSystemMessage = SystemMessage("success", null)
        return objectMapper.writeValueAsString(
            SocketMessagePayload(
                isSysMessage = true,
                systemMessage = successSystemMessage,
                null
            )
        )
    }

    private fun createErrorPayload(message: String): String {
        val errorSystemMessage = SystemMessage("error", message)
        return objectMapper.writeValueAsString(
            SocketMessagePayload(
                isSysMessage = true,
                systemMessage = errorSystemMessage,
                null
            )
        )
    }
}