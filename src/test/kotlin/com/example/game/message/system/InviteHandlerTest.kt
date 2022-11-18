package com.example.game.message.system

import com.example.game.model.SystemMessage
import com.example.game.session.PlayerNotFoundException
import com.example.game.session.SessionHandler
import com.example.game.session.SessionNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

internal class InviteHandlerTest {

    private lateinit var inviteHandler: InviteHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockSession = mockk(relaxed = true)
        mockSessionHandler = mockk(relaxed = true)

        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        inviteHandler = InviteHandler(mockSessionHandler, objectMapper)
    }

    @Test
    fun `handle should return success payload`() {
        val testSystemMessage = SystemMessage("invite", "Bob")
        val resultPayload =
            """{"isSysMessage":true,"systemMessage":{"key":"success","value":null},"gameStateResponse":null}"""
        val expectedResult = TextMessage(resultPayload)

        val result = inviteHandler.handle(mockSession, testSystemMessage)

        verify { mockSessionHandler.createGameSession(mockSession, testSystemMessage) }
        result shouldBe expectedResult
    }

    @Test
    fun `handle should send correct error for missing session`() {
        val testSystemMessage = SystemMessage("invite", "Bob")
        val resultPayload =
            """{"isSysMessage":true,"systemMessage":{"key":"error","value":"Session not found!"},"gameStateResponse":null}"""
        val expectedResult = TextMessage(resultPayload)

        every { mockSessionHandler.createGameSession(any(), testSystemMessage) }.throws(SessionNotFoundException())

        val result = inviteHandler.handle(mockSession, testSystemMessage)

        result shouldBe expectedResult
    }

    @Test
    fun `handle should send correct error for missing player`() {
        val testSystemMessage = SystemMessage("invite", "Bob")
        val resultPayload =
            """{"isSysMessage":true,"systemMessage":{"key":"error","value":"Player not found!"},"gameStateResponse":null}"""
        val expectedResult = TextMessage(resultPayload)

        every { mockSessionHandler.createGameSession(any(), testSystemMessage) }.throws(PlayerNotFoundException())

        val result = inviteHandler.handle(mockSession, testSystemMessage)

        result shouldBe expectedResult
    }
}