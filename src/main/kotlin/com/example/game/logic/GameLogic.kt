package com.example.game.logic

import com.example.game.model.GameStateResponse
import com.example.game.state.GameState
import org.springframework.stereotype.Component

@Component
class GameLogic(private val gameState: GameState) {
    private var winner: Int = 0
    private var isGameOver: Boolean = false

    fun getGameStateResponse(): GameStateResponse {
        winner = checkWinner()
        isGameOver = gameState.isGameEnd() || winner != 0
        return GameStateResponse(gameState = gameState.getGameStatusString(), winner = winner, gameOver = isGameOver)
    }

    private fun checkWinner(): Int {
        val state = gameState.getGameStatus()

        return if (state[0] == state[1] && state[1] == state[2] && state[0] != 0) {
            state[0]
        } else if (state[3] == state[4] && state[4] == state[5] && state[3] != 0) {
            state[3]
        } else if (state[6] == state[7] && state[7] == state[8] && state[6] != 0) {
            state[6]
        } else if (state[0] == state[3] && state[3] == state[6] && state[0] != 0) {
            state[0]
        } else if (state[1] == state[4] && state[4] == state[7] && state[1] != 0) {
            state[1]
        } else if (state[2] == state[5] && state[5] == state[8] && state[2] != 0) {
            state[2]
        } else if (state[0] == state[4] && state[4] == state[8] && state[0] != 0) {
            state[0]
        } else if (state[2] == state[4] && state[4] == state[6] && state[2] != 0) {
            state[2]
        } else {
            0
        }
    }
}