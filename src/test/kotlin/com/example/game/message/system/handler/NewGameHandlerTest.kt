package com.example.game.message.system.handler

import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.example.game.state.GameState
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class NewGameHandlerTest {

    private lateinit var newGameHandler: NewGameHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var gameState: GameState
    private lateinit var newGameService: NewGameService
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        gameState = GameState()
        newGameService = NewGameService(gameState)
        mockSession = mockk(relaxed = true)

        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        newGameHandler = NewGameHandler(newGameService, objectMapper)
    }

    @Test
    fun `handle should return starting game state as payload`() {
        val testSystemMessage = SystemMessage("newGame", null)
        val expectedPayload =
            """{"isSysMessage":true,"systemMessage":null,"gameStateResponse":{"gameState":"0,0,0,0,0,0,0,0,0","winner":0,"gameOver":false}}"""

        val result = newGameHandler.handle(mockSession, testSystemMessage)

        result.payload shouldBe expectedPayload
    }
}