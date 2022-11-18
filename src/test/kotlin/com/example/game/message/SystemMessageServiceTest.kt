package com.example.game.message

import com.example.game.message.system.InvalidInputException
import com.example.game.message.system.SystemMessageHandlerProvider
import com.example.game.message.system.SystemMessageService
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class SystemMessageServiceTest {

    private lateinit var systemMessageService: SystemMessageService
    private lateinit var mockSystemMessageHandlerProvider: SystemMessageHandlerProvider
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        mockSystemMessageHandlerProvider = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)

        systemMessageService = SystemMessageService(
            mockSystemMessageHandlerProvider,
            objectMapper
        )
    }

    @Test
    fun `handleMessage should return invalid input error`() {
        val testSystemMessage = SystemMessage("invalid", null)
        val expectedPayload = """{"isSysMessage":true,"systemMessage":{"key":"error","value":"Invalid input!"},"gameStateResponse":null}"""

        every { mockSystemMessageHandlerProvider.provide(testSystemMessage) }.throws(InvalidInputException())

        val result = systemMessageService.prepareResponseMessage(mockSession, testSystemMessage)

        result.payload shouldBe expectedPayload
    }

    @Test
    fun `handleMessage should call SystemMessageHandlerProvider with correct payload`() {
        val testSystemMessage = SystemMessage("name", "Bob")

        systemMessageService.prepareResponseMessage(mockSession, testSystemMessage)

        verify { mockSystemMessageHandlerProvider.provide(testSystemMessage) }
    }
}