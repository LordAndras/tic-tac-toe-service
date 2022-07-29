package com.example.game.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage

internal class MessageHandlerFacadeTest {
    private lateinit var messageHandlerFacade: MessageHandlerFacade
    private lateinit var mockGameMessageService: GameMessageService
    private lateinit var mockSystemMessageService: SystemMessageService
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockGameMessageService = mockk(relaxed = true)
        mockSystemMessageService = mockk(relaxed = true)
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        messageHandlerFacade = MessageHandlerFacade(mockGameMessageService, mockSystemMessageService, objectMapper)
    }

    @Test
    fun `handleMessage should call handleSystem message if payload's system property is not empty`() {
        val testPayload = """{"system":"greeting","gameStateResponse": null,"error":""}"""

        val testMessage = TextMessage(testPayload)

        messageHandlerFacade.handleMessage(testMessage.payload)

        verify { mockSystemMessageService.handleSystemMessage("greeting") }
    }
}