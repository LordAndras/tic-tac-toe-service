package com.example.game.websocket

import com.example.game.message.MessageHandlerFacade
import com.example.game.model.Player
import com.example.game.sending.MessageSendingService
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
    private val sessionHandler: SessionHandler,
    private val messageSendingService: MessageSendingService
) : TextWebSocketHandler() {
    private companion object {
        val greetingMessage =
            TextMessage("""{"isSysMessage":true,"systemMessage":{"key":"greeting"}, "gameStateResponse": null}""")
        val playersChangedMessage =
            TextMessage("""{"isSysMessage":true,"systemMessage":{"key":"playersChanged"}, "gameStateResponse": null}""")
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val newPlayer = Player(sessionId = session.id)
        sessionHandler.addSession(session, newPlayer)
        session.sendMessage(greetingMessage)
        messageSendingService.messageToAll(sessionHandler.getSessions(), playersChangedMessage)
        super.afterConnectionEstablished(session)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        super.handleMessage(session, message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessionHandler.removeSession(session)
        messageSendingService.messageToAll(sessionHandler.getSessions(), playersChangedMessage)
        super.afterConnectionClosed(session, status)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val responseMessage = try {
            messageHandlerFacade.handleMessage(session, message.payload)
        } catch (exception: Exception) {
            TextMessage("Error: ${exception.message}")
        }

        session.sendMessage(responseMessage)
    }
}