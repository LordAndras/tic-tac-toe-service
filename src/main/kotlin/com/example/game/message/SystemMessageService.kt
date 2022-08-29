package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class SystemMessageService(private val objectMapper: ObjectMapper, private val sessionHandler: SessionHandler) {
    private companion object {
        const val INVALID_INPUT_ERROR = "Invalid input"
        const val MESSAGE_NULL_ERROR = "System message should not be null"
    }

    fun handleSystemMessage(session: WebSocketSession, systemMessage: SystemMessage?): TextMessage {
        return if (systemMessage != null) {
            when (systemMessage.key) {
                "name" -> {
                    systemMessage.value?.let { sessionHandler.setName(session, it) }
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