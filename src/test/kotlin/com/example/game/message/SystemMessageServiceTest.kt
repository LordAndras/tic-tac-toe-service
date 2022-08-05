package com.example.game.message

import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
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
        objectMapper = ObjectMapper()
        systemMessageService = SystemMessageService(objectMapper)
        objectMapper.registerKotlinModule()
    }

    @Test
    fun `handleSystemMessage should return success payload`() {
        val testSystemMessage = SystemMessage("name", null)

        val result = systemMessageService.handleSystemMessage(testSystemMessage)
        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "success"
    }

    @Test
    fun `handleSystemMessage should return invalid input error`() {
        val testSystemMessage = SystemMessage("invalid", null)

        val result = systemMessageService.handleSystemMessage(testSystemMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe "Invalid input"
    }

    @Test
    fun `handleSystemMessage should return message is null error`() {
        val testMessage = null

        val result = systemMessageService.handleSystemMessage(testMessage)

        val resultPayload = objectMapper.readValue(result.payload, socketMessageTypeRef)

        resultPayload.isSysMessage shouldBe true
        resultPayload.systemMessage!!.key shouldBe "error"
        resultPayload.systemMessage!!.value shouldBe  "System message should not be null"
    }

}