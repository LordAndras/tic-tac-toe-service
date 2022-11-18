package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object MessageUtils {
    private val objectMapper: ObjectMapper = ObjectMapper().also { it.registerKotlinModule() }

    fun createSuccessPayload(): String {
        val successMessage = SystemMessage("success", null)
        return objectMapper.writeValueAsString(
            SocketMessagePayload(
                isSysMessage = true,
                systemMessage = successMessage,
                gameStateResponse = null
            )
        )
    }

    fun createErrorPayload(message: String): String {
        val errorSystemMessage = SystemMessage("error", message)
        return objectMapper.writeValueAsString(
            SocketMessagePayload(
                isSysMessage = true,
                systemMessage = errorSystemMessage,
                null
            )
        )
    }
}