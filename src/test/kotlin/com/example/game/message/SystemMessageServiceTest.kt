package com.example.game.message

import com.example.game.message.system.SystemMessageService
import com.example.game.message.system.handler.InviteHandler
import com.example.game.message.system.handler.NameHandler
import com.example.game.message.system.handler.NewGameHandler
import com.example.game.message.system.handler.PlayersHandler
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.WebSocketSession

internal class SystemMessageServiceTest {

    private lateinit var systemMessageService: SystemMessageService
    private lateinit var mockNameHandler: NameHandler
    private lateinit var mockNewGameHandler: NewGameHandler
    private lateinit var mockInviteHandler: InviteHandler
    private lateinit var mockPlayersHandler: PlayersHandler
    private lateinit var mockSessionHandler: SessionHandler
    private lateinit var mockSession: WebSocketSession
    private lateinit var objectMapper: ObjectMapper
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    @BeforeEach
    fun setUp() {
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()

        mockNameHandler = mockk(relaxed = true)
        mockInviteHandler = mockk(relaxed = true)
        mockPlayersHandler = mockk(relaxed = true)
        mockNewGameHandler = mockk(relaxed = true)
        mockSessionHandler = mockk(relaxed = true)
        mockSession = mockk(relaxed = true)

        systemMessageService = SystemMessageService(
            mockNameHandler,
            mockInviteHandler,
            mockPlayersHandler,
            mockNewGameHandler,
            objectMapper
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
    fun `handleMessage should call newGameHandler with correct payload`() {
        val testSystemMessage = SystemMessage("newGame", null)

        systemMessageService.handleMessage(mockSession, testSystemMessage)

        verify { mockNewGameHandler.handle(mockSession, testSystemMessage) }
    }
}