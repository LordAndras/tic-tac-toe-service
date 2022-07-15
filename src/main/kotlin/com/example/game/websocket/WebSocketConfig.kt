package com.example.game.websocket

import com.example.game.message.MessageHandlerService
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WebSocketConfig(private val messageHandlerService: MessageHandlerService) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebsocketHandler(messageHandlerService), "/player").setAllowedOrigins("http://localhost:8080")
    }
}