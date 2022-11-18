package com.example.game.message.system.handler

import com.example.game.message.MessageUtils
import com.example.game.message.system.SystemMessageHandler
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

class NameHandler(private val sessionHandler: SessionHandler) :
    SystemMessageHandler {
    private companion object {
        const val NAME_NULL_ERROR = "Name should not be null"
    }

    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        var payload = MessageUtils.createSuccessPayload()
        if (systemMessage.value != null) {
            sessionHandler.setPlayerName(session, systemMessage.value)
        } else {
            payload = MessageUtils.createErrorPayload(NAME_NULL_ERROR)
        }
        return TextMessage(payload)
    }
}