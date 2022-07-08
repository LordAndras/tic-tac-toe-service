package com.example.game.message

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper

class MessageHandlerService(private val objectMapper: ObjectMapper) {

    fun prepareResponseMessage(incomingPayload: String): String {
        return "Response"
    }



    private fun parseJsonFromPayload(payload: String): JsonNode {
        return objectMapper.readValue(payload, JsonNode::class.java)
    }
}