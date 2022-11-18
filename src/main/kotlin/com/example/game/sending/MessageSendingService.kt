package com.example.game.sending

import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class MessageSendingService() {
    fun sendMessage(session: WebSocketSession, message: TextMessage) {
        session.sendMessage(message)
    }

    fun messageToAll(sessions: MutableSet<WebSocketSession>, message: TextMessage) {
        sessions.forEach {
            it.sendMessage(message)
        }
    }
}