package com.example.game.websocket

import com.example.game.message.MessageHandlerFacade
import com.example.game.session.SessionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WebSocketConfig(private val messageHandlerFacade: MessageHandlerFacade, private val sessionHandler: SessionHandler) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebsocketHandler(messageHandlerFacade, sessionHandler), "/player")
            .setAllowedOrigins("*")
    }
}