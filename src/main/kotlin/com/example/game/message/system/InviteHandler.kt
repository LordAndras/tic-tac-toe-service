package com.example.game.message.system

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
        sessionHandler.createGameSession(session, systemMessage)
        val json = objectMapper.writeValueAsString(createSuccessPayload())
        return TextMessage(json)
    }

    private fun createSuccessPayload(): SocketMessagePayload {
        val successSystemMessage = SystemMessage("success", null)
        return SocketMessagePayload(isSysMessage = true, systemMessage = successSystemMessage)
    }
}