package com.example.game.message

import com.example.game.model.GameStateResponse
import com.example.game.model.SocketMessagePayload
import com.example.game.service.ProcessNextStepService
import com.example.game.service.ValidateStepService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class GameMessageService(
    private val objectMapper: ObjectMapper,
    private val validateStepService: ValidateStepService,
    private val processNextStepService: ProcessNextStepService
) {

    fun prepareResponseMessage(incomingPayload: String): TextMessage {
        val isValidInput = validateStepService.validate(incomingPayload)
        return if (isValidInput) {
            val gameStateResponse = processNextStepService.nextStep(incomingPayload)
            TextMessage(createGameStatusPayload(gameStateResponse))
        } else {
            TextMessage(MessageUtils.createErrorPayload("Invalid input!"))
        }
    }

    private fun createGameStatusPayload(gameStateResponse: GameStateResponse): String {
        return objectMapper.writeValueAsString(SocketMessagePayload(gameStateResponse = gameStateResponse))
    }
}