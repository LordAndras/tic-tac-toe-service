package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class SystemMessageService(private val objectMapper: ObjectMapper) {
    private companion object {
        const val INVALID_INPUT_ERROR = "Invalid input"
        const val MESSAGE_NULL_ERROR = "System message should not be null"
    }

    fun handleSystemMessage(systemMessage: SystemMessage?): TextMessage {
        return if (systemMessage != null) {
            when (systemMessage.key) {
                "name" -> {
                    val payload = createSuccessPayload()
                    val json = jacksonObjectMapper().writeValueAsString(payload)
                    TextMessage(json)
                }
                else -> {
                    TextMessage(objectMapper.writeValueAsString(createErrorPayload(INVALID_INPUT_ERROR)))
                }
            }
        } else {
            TextMessage(objectMapper.writeValueAsString(createErrorPayload(MESSAGE_NULL_ERROR)))
        }
    }

    private fun createSuccessPayload(): SocketMessagePayload {
        val successSystemMessage = SystemMessage("success", null)
        return SocketMessagePayload(isSysMessage = true, systemMessage = successSystemMessage)
    }

    private fun createErrorPayload(message: String): SocketMessagePayload {
        val errorSystemMessage = SystemMessage("error", message)
        return SocketMessagePayload(isSysMessage = true, systemMessage = errorSystemMessage)
    }
}