package com.example.game.message

import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class SystemMessageService {
    fun handleSystemMessage(systemMessage: String): TextMessage {
        return TextMessage(systemMessage)
    }
}