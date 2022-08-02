package com.example.game.websocket

import com.example.game.message.MessageHandlerFacade
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WebSocketConfig(private val messageHandlerFacade: MessageHandlerFacade) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebsocketHandler(messageHandlerFacade), "/player")
            .setAllowedOrigins("http://localhost:8080")
    }
}