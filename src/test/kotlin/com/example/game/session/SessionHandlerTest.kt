package com.example.game.session

import com.example.game.model.Player
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.socket.WebSocketSession

internal class SessionHandlerTest {
    private companion object {
        const val TEST_SESSION_ID = "testId"
    }

    private lateinit var mockWebSocketSession: WebSocketSession
    private lateinit var mockPlayer: Player
    private lateinit var sessionHandler: SessionHandler

    @BeforeEach
    fun setUp() {
        mockWebSocketSession = mockk(relaxed = true)
        every { mockWebSocketSession.id } returns TEST_SESSION_ID

        mockPlayer = mockk(relaxed = true)
        sessionHandler = SessionHandler()
    }

    @Test
    fun `getSessionFromId should return session with correct Id`() {
        sessionHandler.addSession(mockWebSocketSession, mockPlayer)

        val result = sessionHandler.getSessionFromId(TEST_SESSION_ID)

        result shouldBe mockWebSocketSession
    }

    @Test
    fun `getSessionFromId should throw SessionNotFoundException when session with required Id is missing`() {
        every { mockWebSocketSession.id } returns "you wont find this"

        sessionHandler.addSession(mockWebSocketSession, mockPlayer)

        assertThrows<SessionNotFoundException> { sessionHandler.getSessionFromId(TEST_SESSION_ID) }
    }

    @Test
    fun `getPlayerOfSession should return the correct player`() {
        sessionHandler.addSession(mockWebSocketSession, mockPlayer)

        val result = sessionHandler.getPlayerOfSession(mockWebSocketSession)

        result shouldBe mockPlayer
    }

    @Test
    fun `getPlayerOfSession should throw PlayerNotFoundException when player with session is missing`() {
        val anotherSession: WebSocketSession = mockk(relaxed = true)
        sessionHandler.addSession(mockWebSocketSession, mockPlayer)

        assertThrows<PlayerNotFoundException> { sessionHandler.getPlayerOfSession(anotherSession) }
    }
}