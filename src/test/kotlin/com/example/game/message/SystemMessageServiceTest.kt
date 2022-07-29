package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SystemMessageServiceTest {
    private lateinit var systemMessageService: SystemMessageService
    private lateinit var objectMapper: ObjectMapper
    private val socketMessageTypeRef = object : TypeReference<SocketMessagePayload>() {}

    @BeforeEach
    fun setUp() {
        systemMessageService = SystemMessageService()
        objectMapper = ObjectMapper()
        objectMapper.registerKotlinModule()
    }

    @Test
    fun `handleSystemMessage should return greeting payload`() {
        val testMessage = "greeting"

        val result = systemMessageService.handleSystemMessage(testMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.system shouldBe "greeting"
    }

    @Test
    fun `handleSystemMessage should return newGame payload`() {
        val testMessage = "name"

        val result = systemMessageService.handleSystemMessage(testMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.system shouldBe "newGame"
    }

    @Test
    fun `handleSystemMessage should return error payload`() {
        val testMessage = "invalid"

        val result = systemMessageService.handleSystemMessage(testMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.error shouldBe "unsupported function"
        resultPayload.system shouldBe ""
    }

}