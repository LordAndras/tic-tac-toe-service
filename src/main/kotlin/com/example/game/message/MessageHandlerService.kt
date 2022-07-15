package com.example.game.message

import com.example.game.model.GameStateResponse
import com.example.game.service.ProcessNextStepService
import com.example.game.service.ValidateStepService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service

@Service
class MessageHandlerService(
    private val objectMapper: ObjectMapper,
    private val validateStepService: ValidateStepService,
    private val processNextStepService: ProcessNextStepService
) {

    fun prepareResponseMessage(incomingPayload: String): String {
        val isValidInput = validateStepService.validate(incomingPayload)
        return if (isValidInput) {
            val gameStateResponse = processNextStepService.processNextStep(incomingPayload)
            createMessagePayload(gameStateResponse)
        } else {
            ""
        }
    }

    private fun createMessagePayload(gameStateResponse: GameStateResponse): String {
        return objectMapper.writeValueAsString(gameStateResponse)
    }
}