package com.example.game.message

import com.example.game.message.system.SystemMessageService
import com.example.game.model.GameStateResponse
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.socket.WebSocketSession

internal class MessageHandlerFacadeTest {
    private lateinit var messageHandlerFacade: MessageHandlerFacade
    private lateinit var mockGameMessageService: GameMessageService
    private lateinit var mockSystemMessageService: SystemMessageService
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockGameMessageService = mockk(relaxed = true)
        mockSystemMessageService = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        messageHandlerFacade = MessageHandlerFacade(mockGameMessageService, mockSystemMessageService, objectMapper)
    }

    @Test
    fun `handleMessage should call handleSystem message if payload is system message`() {
        val testSystemMessage = SystemMessage("name", "Bob")

        val testPayload = SocketMessagePayload(true, testSystemMessage)

        messageHandlerFacade.handleMessage(mockSession, objectMapper.writeValueAsString(testPayload))

        verify { mockSystemMessageService.handleMessage(mockSession, testSystemMessage) }
    }

    @Test
    fun `handleMessage should not call handleSystem message if payload is not system message`() {
        val testSystemMessage = SystemMessage("name", "Bob")

        val testPayload = SocketMessagePayload(false, testSystemMessage, gameStateResponse = GameStateResponse())

        messageHandlerFacade.handleMessage(mockSession, objectMapper.writeValueAsString(testPayload))

        verify(exactly = 0) { mockSystemMessageService.handleMessage(mockSession, testSystemMessage) }
    }

    @Test
    fun `handleMessage should throw payload is null exception`() {
        val testMessage = """{"isSysMessage":true,"systemMessage":null, "gameStateResponse": null}"""

        assertThrows<MessagePayloadIsNullException> {
            messageHandlerFacade.handleMessage(mockSession, testMessage)
        }
    }
}