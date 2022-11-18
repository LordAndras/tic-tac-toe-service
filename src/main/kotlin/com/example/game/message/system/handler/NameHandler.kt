package com.example.game.message.system.handler

import com.example.game.message.system.SystemMessageHandler
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class NameHandler(private val sessionHandler: SessionHandler, private val objectMapper: ObjectMapper) :
    SystemMessageHandler {
    private companion object {
        const val NAME_NULL_ERROR = "Name should not be null"
    }

    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        var payload = createSuccessPayload()
        if (systemMessage.value != null) {
            sessionHandler.setPlayerName(session, systemMessage.value)
        } else {
            payload = createErrorPayload(NAME_NULL_ERROR)
        }
        val json = objectMapper.writeValueAsString(payload)
        return TextMessage(json)
    }

    private fun createSuccessPayload(): SocketMessagePayload {
        val successMessage = SystemMessage("success", null)
        return SocketMessagePayload(isSysMessage = true, systemMessage = successMessage, gameStateResponse = null)
    }

    private fun createErrorPayload(message: String): SocketMessagePayload {
        val errorSystemMessage = SystemMessage("error", message)
        return SocketMessagePayload(isSysMessage = true, systemMessage = errorSystemMessage)
    }

}