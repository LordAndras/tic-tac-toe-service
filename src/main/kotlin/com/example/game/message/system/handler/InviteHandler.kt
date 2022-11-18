package com.example.game.message.system.handler

import com.example.game.message.MessageUtils
import com.example.game.message.system.SystemMessageHandler
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class InviteHandler(private val sessionHandler: SessionHandler) :
    SystemMessageHandler {
    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        return try {
            sessionHandler.createGameSession(session, systemMessage)
            TextMessage(MessageUtils.createSuccessPayload())
        } catch (exception: Exception) {
            TextMessage(MessageUtils.createErrorPayload(exception.message ?: "Error occurred"))
        }
    }
}