package com.example.game.websocket

import com.example.game.message.MessageHandlerFacade
import com.example.game.model.Player
import com.example.game.session.SessionHandler
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class WebsocketHandler(
    private val messageHandlerFacade: MessageHandlerFacade,
    private val sessionHandler: SessionHandler
) : TextWebSocketHandler() {
    private companion object {
        val greetingMessage = TextMessage("""{"isSysMessage":true,"systemMessage":{"key":"greeting"}, "gameStateResponse": null}""")
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val newPlayer = Player(sessionId = session.id)
        sessionHandler.addSession(session, newPlayer)
        session.sendMessage(greetingMessage)
        super.afterConnectionEstablished(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        super.handleMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionHandler.removeSession(session)
        super.afterConnectionClosed(session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val responseMessage = messageHandlerFacade.handleMessage(session, message.payload)

        sessionHandler.getSessions().keys.forEach {
            it.sendMessage(responseMessage)
        }
    }
}