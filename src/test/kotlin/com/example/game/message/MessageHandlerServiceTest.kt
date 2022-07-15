package com.example.game.message

import com.example.game.model.GameStateResponse
import com.example.game.service.ProcessNextStepService
import com.example.game.service.ValidateStepService
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class MessageHandlerServiceTest {

    private lateinit var messageHandlerService: MessageHandlerService
    private lateinit var mockObjectMapper: ObjectMapper
    private lateinit var mockValidateStepService: ValidateStepService
    private lateinit var mockProcessNextStepService: ProcessNextStepService

    @BeforeEach
    fun setup() {
        mockObjectMapper = mockk(relaxed = true)
        mockValidateStepService = mockk(relaxed = true)
        mockProcessNextStepService = mockk(relaxed = true)
        messageHandlerService = MessageHandlerService(mockObjectMapper, mockValidateStepService, mockProcessNextStepService)
    }

    @Test
    fun `prepareResponseMessage should validate incoming payload`() {
        val testInput = "input"

        messageHandlerService.prepareResponseMessage(testInput)

        verify { mockValidateStepService.validate(testInput) }
    }

    @Test
    fun `prepareResponseMessage should call processNextStep if input is valid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"

        every { mockValidateStepService.validate(any()) } returns true

        messageHandlerService.prepareResponseMessage(testInput)

        verify { mockProcessNextStepService.processNextStep(testInput) }
    }

    @Test
    fun `prepareResponseMessage should not call processNextStep if input is invalid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"

        every { mockValidateStepService.validate(any()) } returns false

        messageHandlerService.prepareResponseMessage(testInput)

        verify(exactly = 0) { mockProcessNextStepService.processNextStep(testInput) }
    }

    @Test
    fun `prepareResponseMessage should return correct gameStateResponse`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"
        val expectation = GameStateResponse("0,0,0,0,0,0,0,0,0", 1, true)

        every { mockValidateStepService.validate(any()) } returns true
        every { mockProcessNextStepService.processNextStep(testInput) } returns expectation
        every { mockObjectMapper.writeValueAsString(expectation) } returns "success"

       val result = messageHandlerService.prepareResponseMessage(testInput)

        result shouldBe "success"
    }

    @Test
    fun `prepareResponseMessage should return empty string if input is invalid`() {
        val testInput = "0,1,1,0,-1,0,-1,1,1"
        val expectation = GameStateResponse("0,0,0,0,0,0,0,0,0", 1, true)

        every { mockValidateStepService.validate(any()) } returns false
        every { mockProcessNextStepService.processNextStep(testInput) } returns expectation
        every { mockObjectMapper.writeValueAsString(expectation) } returns "success"

       val result = messageHandlerService.prepareResponseMessage(testInput)

        result shouldBe ""
    }

}