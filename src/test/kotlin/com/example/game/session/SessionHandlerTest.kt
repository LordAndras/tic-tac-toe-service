package com.example.game.session

import com.example.game.model.Player
import com.example.game.model.SystemMessage
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.web.socket.WebSocketSession

internal class SessionHandlerTest {
    private companion object {
        const val TEST_SESSION_ID = "test Id"
        const val TEST_SESSION_ID_2 = "test Id 2"
        const val TEST_NAME = "Bob"
    }

    private lateinit var mockWebSocketSession: WebSocketSession
    private lateinit var mockWebSocketSession2: WebSocketSession
    private lateinit var sessionHandler: SessionHandler
    private lateinit var testSystemMessage: SystemMessage

    @BeforeEach
    fun setUp() {
        mockWebSocketSession = mockk(relaxed = true)
        every { mockWebSocketSession.id } returns TEST_SESSION_ID
        mockWebSocketSession2 = mockk(relaxed = true)
        every { mockWebSocketSession2.id } returns TEST_SESSION_ID_2

        sessionHandler = SessionHandler()

        testSystemMessage = SystemMessage("invite", TEST_SESSION_ID_2)
    }

    @Test
    fun `setPlayerName should set the name of the correct player`() {
        val testPlayer = Player(null, TEST_SESSION_ID)
        sessionHandler.addSession(mockWebSocketSession, testPlayer)
        sessionHandler.setPlayerName(mockWebSocketSession, TEST_NAME)

        testPlayer.name shouldBe TEST_NAME
    }

    @Test
    fun `setPlayerName should throw PlayerNotFoundException when player is missing`() {
        val player = Player()
        val anotherSession: WebSocketSession = mockk(relaxed = true)

        sessionHandler.addSession(mockWebSocketSession, player)

        assertThrows<PlayerNotFoundException> { sessionHandler.setPlayerName(anotherSession, TEST_NAME) }
    }

    @Test
    fun `createGameSession should create the gameSession with the correct players`() {
        val player = Player(inGame = false)
        val player2 = Player(inGame = false)

        sessionHandler.addSession(mockWebSocketSession, player)
        sessionHandler.addSession(mockWebSocketSession2, player2)

        sessionHandler.getCurrentGameNumber() shouldBe 0

        sessionHandler.createGameSession(mockWebSocketSession, testSystemMessage)

        sessionHandler.getCurrentGameNumber() shouldBe 1
        player.inGame shouldBe true
        player2.inGame shouldBe true
    }

    @Test
    fun `createGameSession should throw SessionNotFoundException when session with id missing`() {
        val player = Player()
        val anotherSystemMessage = SystemMessage("invite", "you wont find this")
        sessionHandler.addSession(mockWebSocketSession, player)

        assertThrows<SessionNotFoundException> {
            sessionHandler.createGameSession(
                mockWebSocketSession,
                anotherSystemMessage
            )
        }
    }

    @Test
    fun `createGameSession should throw PlayerNotFoundException when player with session missing`() {
        assertThrows<PlayerNotFoundException> {
            sessionHandler.createGameSession(
                mockWebSocketSession,
                testSystemMessage
            )
        }
    }
}