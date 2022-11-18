package com.example.game.sending

import com.example.game.model.Player
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class MessageSendingService(private val objectMapper: ObjectMapper) {
    fun sendMessage(session: WebSocketSession, message: TextMessage) {
        session.sendMessage(message)
    }

    fun messageToAll(sessions: MutableSet<WebSocketSession>, message: TextMessage) {
        sessions.forEach {
            it.sendMessage(message)
        }
    }

    fun sendInvite(session: WebSocketSession, player: Player) {
        session.sendMessage(createInvitation(player.name!!))
    }

    private fun createInvitation(playerName: String): TextMessage {
        val payload = SocketMessagePayload(true, SystemMessage("invite", playerName))
        return TextMessage(objectMapper.writeValueAsString(payload))
    }
}