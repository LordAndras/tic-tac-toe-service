package com.example.game.websocket

import com.example.game.message.MessageHandlerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
open class WebSocketConfig : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(webSocketHandler(), "/player").setAllowedOrigins("http://localhost:8080")
    }

    @Bean
    open fun webSocketHandler(): WebsocketHandler {
        return WebsocketHandler(messageHandlerService())
    }

    @Bean
    open fun messageHandlerService(): MessageHandlerService {
        return MessageHandlerService(objectMapper())
    }

    @Bean
    open fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}