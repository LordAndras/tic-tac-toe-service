package com.example.game.websocket

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.example.game.model.Player
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebsocketHandler : TextWebSocketHandler() {
    private val objectMapper = ObjectMapper()
    private val sessions = mutableMapOf<WebSocketSession, Player>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val newPlayer = Player(null, session.id)
        sessions[session] = newPlayer
        super.afterConnectionEstablished(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        super.handleMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session)
        super.afterConnectionClosed(session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val jsonPayload = objectMapper.readValue(payload, JsonNode::class.java)

        sessions.keys.forEach {
            it.sendMessage(TextMessage(jsonPayload.asText()))
        }
    }
}