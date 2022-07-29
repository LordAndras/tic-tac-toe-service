package com.example.game.message

import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class SystemMessageService {
    private companion object {
        const val GREETING_PAYLOAD = """{
            "system":"greeting"
            }"""
        const val NEW_GAME_PAYLOAD = """{
            "system":"newGame"
            }"""
        const val ERROR_PAYLOAD = """{
            "error":"unsupported function"
            }"""
    }

    fun handleSystemMessage(systemMessage: String): TextMessage {
        return when (systemMessage) {
            "name" -> {
                TextMessage(NEW_GAME_PAYLOAD)
            }
            "greeting" -> {
                TextMessage(GREETING_PAYLOAD)
            }
            else -> {
                TextMessage(ERROR_PAYLOAD)
            }
        }
    }
}