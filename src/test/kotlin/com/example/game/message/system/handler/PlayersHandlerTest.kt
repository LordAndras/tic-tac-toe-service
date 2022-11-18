package com.example.game.message.system.handler

import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class PlayersHandlerTest {

    private lateinit var playersHandler: PlayersHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var objectMapper: ObjectMapper


    @BeforeEach
    fun setUp() {
        mockSession = mockk(relaxed = true)
        mockSessionHandler = mockk(relaxed = true)

        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        playersHandler = PlayersHandler(mockSessionHandler, objectMapper)
    }

    @Test
    fun `handle should return the list of player names`() {
        val testSystemMessage = SystemMessage("players")
        val expectedPlayers = mutableListOf("Bob", "Tom")

        every { mockSessionHandler.getPlayers() }.returns(expectedPlayers)

        val expectedPayload = """{"isSysMessage":true,"systemMessage":{"key":"success","value":"[\"Bob\",\"Tom\"]"},"gameStateResponse":null}"""

        val result = playersHandler.handle(mockSession, testSystemMessage)

        result.payload shouldBe expectedPayload
    }
}