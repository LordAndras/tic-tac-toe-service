package com.example.game.message

import com.example.game.message.system.SystemMessageService
import com.example.game.message.system.handler.InviteHandler
import com.example.game.message.system.handler.NameHandler
import com.example.game.message.system.handler.PlayersHandler
import com.example.game.model.GameStateResponse
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
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class SystemMessageServiceTest {

    private lateinit var systemMessageService: SystemMessageService
    private lateinit var mockNewGameService: NewGameService
    private lateinit var mockNameHandler: NameHandler
    private lateinit var mockInviteHandler: InviteHandler
    private lateinit var mockPlayersHandler: PlayersHandler
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper
    private val socketMessageTypeRef = object: TypeReference<SocketMessagePayload>() {}

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
        mockSessionHandler = mockk(relaxed = true)
        mockNameHandler = mockk(relaxed = true)
        mockPlayersHandler = mockk(relaxed = true)
        mockInviteHandler = mockk(relaxed = true)
        mockNewGameService = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)
        systemMessageService = SystemMessageService(
            mockSessionHandler,
            mockNameHandler,
            mockInviteHandler,
            mockPlayersHandler,
            mockNewGameService,
            objectMapper,
        )
    }

    @Test
    fun `handleMessage should return invalid input error`() {
        val testSystemMessage = SystemMessage("invalid", null)

        val result = systemMessageService.handleMessage(mockSession, testSystemMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe "Invalid input"
    }

    @Test
    fun `handleMessage should call nameHandler with correct payload`() {
        val testSystemMessage = SystemMessage("name", "Bob")

        systemMessageService.handleMessage(mockSession, testSystemMessage)

        verify { mockNameHandler.handle(mockSession, testSystemMessage) }
    }

    @Test
    fun `handleMessage should call playersHandler with correct payload`() {
        val testSystemMessage = SystemMessage("players", null)

        systemMessageService.handleMessage(mockSession, testSystemMessage)

        verify { mockPlayersHandler.handle(mockSession, testSystemMessage) }
    }

    @Test
    fun `handleMessage should call inviteHandler with correct payload`() {
        val testSystemMessage = SystemMessage("invite", "Bob")

        systemMessageService.handleMessage(mockSession, testSystemMessage)

        verify { mockInviteHandler.handle(mockSession, testSystemMessage) }
    }

    @Test
    fun `handleMessage should return a new game's empty state`() {
        val testSystemMessage = SystemMessage("newGame", null)
        val expectedResult = GameStateResponse("test state")

        every { mockNewGameService.newGame() } returns expectedResult

        val result = systemMessageService.handleMessage(mockSession, testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.gameStateResponse shouldBe expectedResult
    }
}