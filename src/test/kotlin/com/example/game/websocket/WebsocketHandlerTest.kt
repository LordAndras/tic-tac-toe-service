package com.example.game.websocket

import com.example.game.message.MessageHandlerFacade
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession


internal class WebsocketHandlerTest {

    private lateinit var websocketHandler: WebsocketHandler
    private lateinit var messageHandlerFacade: MessageHandlerFacade
    private lateinit var mockSession: WebSocketSession

    @BeforeEach
    fun setUp() {
        messageHandlerFacade = mockk(relaxed = true)
        websocketHandler = WebsocketHandler(messageHandlerFacade)
        mockSession = mockk(relaxed = true)
    }

    @Test
    fun `handleTextMessage should call prepareResponseMessage messageHandlerService with payload`() {
        val testTextMessage = TextMessage("testPayload")

        websocketHandler.handleMessage(mockSession, testTextMessage)

        verify { messageHandlerFacade.handleMessage(testTextMessage.payload) }
    }

}