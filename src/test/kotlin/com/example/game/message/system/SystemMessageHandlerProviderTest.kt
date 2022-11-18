package com.example.game.message.system

import com.example.game.message.system.handler.InviteHandler
import com.example.game.message.system.handler.NameHandler
import com.example.game.message.system.handler.NewGameHandler
import com.example.game.message.system.handler.PlayersHandler
import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.socket.WebSocketSession

internal class SystemMessageHandlerProviderTest {
    private lateinit var systemMessageHandlerProvider: SystemMessageHandlerProvider
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var mockNewGameService: NewGameService
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        mockSession = mockk(relaxed = true)
        mockSessionHandler = mockk(relaxed = true)
        mockNewGameService = mockk(relaxed = true)
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        systemMessageHandlerProvider =
            SystemMessageHandlerProvider(mockSessionHandler, mockNewGameService, objectMapper)
    }

    @Test
    fun `provide should return nameHandler`() {
        val testSystemMessage = SystemMessage("name", "Bob")

        val result = systemMessageHandlerProvider.provide(testSystemMessage)

        result::class shouldBe NameHandler::class
    }

    @Test
    fun `provide should return newGameHandler`() {
        val testSystemMessage = SystemMessage("newGame", null)

        val result = systemMessageHandlerProvider.provide(testSystemMessage)

        result::class shouldBe NewGameHandler::class
    }

    @Test
    fun `provide should return playersHandler`() {
        val testSystemMessage = SystemMessage("players", null)

        val result = systemMessageHandlerProvider.provide(testSystemMessage)

        result::class shouldBe PlayersHandler::class
    }

    @Test
    fun `provide should return inviteHandler`() {
        val testSystemMessage = SystemMessage("invite", "Bob")

        val result = systemMessageHandlerProvider.provide(testSystemMessage)

        result::class shouldBe InviteHandler::class
    }

    @Test
    fun `provide should throw invalid input exception if message has invalid key`() {
        val testSystemMessage = SystemMessage("invalid", "Bob")

        assertThrows<InvalidInputException> { systemMessageHandlerProvider.provide(testSystemMessage) }
    }
}