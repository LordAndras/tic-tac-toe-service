package com.example.game.message.system

import com.example.game.message.system.handler.NameHandler
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

internal class NameHandlerTest {

    private lateinit var nameHandler: NameHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockSession = mockk(relaxed = true)
        mockSessionHandler = mockk(relaxed = true)

        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        nameHandler = NameHandler(mockSessionHandler, objectMapper)
    }

    @Test
    fun `handleMessage should return success payload with name message`() {
        val testSystemMessage = SystemMessage("name", "Bob")
        val resultPayload =
            """{"isSysMessage":true,"systemMessage":{"key":"success","value":null},"gameStateResponse":null}"""
        val expectedResult = TextMessage(resultPayload)

        val result = nameHandler.handle(mockSession, testSystemMessage)

        verify { mockSessionHandler.setPlayerName(mockSession, "Bob") }
        result shouldBe expectedResult
    }

    @Test
    fun `handleMessage should send name is null error`() {
        val testSystemMessage = SystemMessage("name", null)
        val resultPayload =
            """{"isSysMessage":true,"systemMessage":{"key":"error","value":"Name should not be null"},"gameStateResponse":null}"""
        val expectedResult = TextMessage(resultPayload)

        val result = nameHandler.handle(mockSession, testSystemMessage)

        verify(exactly = 0) { mockSessionHandler.setPlayerName(mockSession, any()) }
        result shouldBe expectedResult
    }
}