package com.example.game.model

data class GameStateResponse(
    val gameState: String = "",
    val winner: Int = 0,
    val gameOver: Boolean = false
)