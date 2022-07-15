package com.example.game.model

data class SocketMessagePayload(
    val system: String = "",
    val gameStateResponse: GameStateResponse? = null,
    val error: String = ""
)