package com.example.game.message.system

import com.example.game.model.SystemMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

interface SystemMessageHandler {
    fun handle(session: WebSocketSession, systemMessage: SystemMessage): TextMessage
}