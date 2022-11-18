package com.example.game.sending

import com.example.game.model.Player
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

internal class MessageSendingServiceTest {
    private val objectMapper = ObjectMapper().also { it.registerKotlinModule() }
    private val messageSendingService: MessageSendingService = MessageSendingService(objectMapper)

    @Test
    fun `sendInvite should send the correct message payload`() {
        val mockSession: WebSocketSession = mockk(relaxed = true)

        val messageSlot = slot<TextMessage>()
        val player = Player("Bob", "test id")
        val expectedPayload = """{"isSysMessage":true,"systemMessage":{"key":"invite","value":"Bob"},"gameStateResponse":null}"""

        every { mockSession.sendMessage(capture(messageSlot)) } just runs

        messageSendingService.sendInvite(mockSession, player)

        messageSlot.captured.payload shouldBe expectedPayload
    }
}