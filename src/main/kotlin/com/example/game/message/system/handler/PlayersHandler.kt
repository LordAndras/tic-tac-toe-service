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
class PlayersHandler(private val sessionHandler: SessionHandler, private val objectMapper: ObjectMapper) :
    SystemMessageHandler {
    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        val players = objectMapper.writeValueAsString(sessionHandler.getPlayers())
        val playerMessage = SystemMessage("success", players)
        val payload = objectMapper.writeValueAsString(SocketMessagePayload(isSysMessage = true, systemMessage = playerMessage))
        return TextMessage(payload)
    }
}