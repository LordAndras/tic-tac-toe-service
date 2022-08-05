package com.example.game.model

data class SocketMessagePayload(
    val isSysMessage: Boolean = false,
    val systemMessage: SystemMessage? = null,
    val gameStateResponse: GameStateResponse? = null
)