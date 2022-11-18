package com.example.game.message

import com.example.game.model.GameStateResponse
import com.example.game.model.SocketMessagePayload
import com.example.game.service.ProcessNextStepService
import com.example.game.service.ValidateStepService
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameMessageServiceTest {

    private lateinit var gameMessageService: GameMessageService
    private lateinit var mockObjectMapper: ObjectMapper
    private lateinit var mockValidateStepService: ValidateStepService
    private lateinit var mockProcessNextStepService: ProcessNextStepService

    @BeforeEach
    fun setup() {
        mockObjectMapper = mockk(relaxed = true)
        mockValidateStepService = mockk(relaxed = true)
        mockProcessNextStepService = mockk(relaxed = true)
        gameMessageService = GameMessageService(mockObjectMapper, mockValidateStepService, mockProcessNextStepService)
    }

    @Test
    fun `prepareResponseMessage should validate incoming payload`() {
        val testInput = "input"

        gameMessageService.prepareResponseMessage(testInput)

        verify { mockValidateStepService.validate(testInput) }
    }

    @Test
    fun `prepareResponseMessage should call processNextStep if input is valid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"

        every { mockValidateStepService.validate(any()) } returns true

        gameMessageService.prepareResponseMessage(testInput)

        verify { mockProcessNextStepService.nextStep(testInput) }
    }

    @Test
    fun `prepareResponseMessage should not call processNextStep if input is invalid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"

        every { mockValidateStepService.validate(any()) } returns false

        gameMessageService.prepareResponseMessage(testInput)

        verify(exactly = 0) { mockProcessNextStepService.nextStep(testInput) }
    }

    @Test
    fun `prepareResponseMessage should return correct gameStateResponse as payload`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"
        val testGameStateResponse = GameStateResponse("0,0,0,0,0,0,0,0,0", 1, true)
        val expectation = SocketMessagePayload(gameStateResponse = testGameStateResponse)

        every { mockValidateStepService.validate(testInput) } returns true
        every { mockProcessNextStepService.nextStep(testInput) } returns testGameStateResponse
        every { mockObjectMapper.writeValueAsString(expectation) } returns "success"

       val result = gameMessageService.prepareResponseMessage(testInput)

        result.payload shouldBe "success"
    }

    @Test
    fun `prepareResponseMessage should return error message as payload string if input is invalid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"
        val expectedPayload = """{"isSysMessage":true,"systemMessage":{"key":"error","value":"Invalid input!"},"gameStateResponse":null}"""

        every { mockValidateStepService.validate(testInput) } returns false

       val result = gameMessageService.prepareResponseMessage(testInput)

        result.payload shouldBe expectedPayload
    }

}