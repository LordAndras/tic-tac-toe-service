package com.example.game.message

import com.example.game.model.GameStateResponse
import com.example.game.model.Player
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class SystemMessageServiceTest {
    private companion object {
        const val SESSION_ID = "test id"
    }
    private lateinit var systemMessageService: SystemMessageService
    private lateinit var mockNewGameService: NewGameService
    private lateinit var sessionHandler: SessionHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        sessionHandler = SessionHandler(mockk(relaxed = true))
        mockNewGameService = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)
        systemMessageService = SystemMessageService(sessionHandler, mockNewGameService, objectMapper)
        objectMapper.registerKotlinModule()
    }

    @Test
    fun `handleSystemMessage should return success payload with name message`() {
        val testPlayer = Player(sessionId = SESSION_ID)
        sessionHandler.addSession(mockSession, testPlayer)
        val testSystemMessage = SystemMessage("name", "Bob")

        val result = systemMessageService.handleSystemMessage(mockSession, testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "success"
    }

    @Test
    fun `handleSystemMessage should return invalid input error`() {
        val testSystemMessage = SystemMessage("invalid", null)

        val result = systemMessageService.handleSystemMessage(mockSession, testSystemMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe "Invalid input"
    }

    @Test
    fun `handleSystemMessage should return message is null error`() {
        val testMessage = null

        val result = systemMessageService.handleSystemMessage(mockSession, testMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe "System message should not be null"
    }

    @Test
    fun `handleSystemMessage should set the players name in sessions`() {
        every { mockSession.id } returns "testId"
        val testName = "Bob"
        val session2: WebSocketSession = mockk(relaxed = true) {
           every { id } returns "sessionId"
        }

        sessionHandler.addSession(mockSession, Player(null, "testId"))
        sessionHandler.addSession(session2, Player(null, "sessionId"))

        val testMessage = SystemMessage("name", testName)

        systemMessageService.handleSystemMessage(mockSession, testMessage)

        sessionHandler.getSessions()[mockSession]!!.name shouldBe testName
    }

    @Test
    fun `handleSystemMessage should send name is null error`() {
        val testSystemMessage = SystemMessage("name", null)

        val result = systemMessageService.handleSystemMessage(mockSession, testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe "Name should not be null"
    }

    @Test
    fun `handleSystemMessage should return list of players`() {
        val testPlayer1 = Player("Bob", "testId")
        val testPlayer2 = Player("Tom", "testId")

        val testSystemMessage = SystemMessage("players", null)

        every { mockSession.id } returns "testId"
        val session2: WebSocketSession = mockk(relaxed = true) {
            every { id } returns "sessionId"
        }

        sessionHandler.addSession(mockSession, testPlayer1)
        sessionHandler.addSession(session2, testPlayer2,)


        val result = systemMessageService.handleSystemMessage(mockSession, testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.value shouldBe objectMapper.writeValueAsString(listOf(testPlayer1, testPlayer2))
    }

    @Test
    fun `handleSystemMessage should return a new game's empty state`() {
        val testSystemMessage = SystemMessage("newGame", null)
        val expectedResult = GameStateResponse("test state")

        every { mockNewGameService.newGame() } returns expectedResult

        val result = systemMessageService.handleSystemMessage(mockSession, testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.gameStateResponse shouldBe expectedResult
    }

}