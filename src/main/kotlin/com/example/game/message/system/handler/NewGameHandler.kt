package com.example.game.message.system.handler

import com.example.game.message.system.SystemMessageHandler
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class NewGameHandler(private val newGameService: NewGameService, private val objectMapper: ObjectMapper) :
    SystemMessageHandler {
    override fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        val gameStateResponse = newGameService.newGame()
        val payload = objectMapper.writeValueAsString(SocketMessagePayload(true, null, gameStateResponse = gameStateResponse))
        return TextMessage(payload)
    }
}