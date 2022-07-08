package com.example.game.logic

import com.example.game.model.GameStateResponse
import com.example.game.state.GameState
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameLogicTest {
    private lateinit var gameState: GameState

    private companion object {
        const val NO_WINNER_END_STATE = "-1,1,-1,-1,1,1,1,-1,1"
        const val WINNER_WITH_NOT_FULL_BOARD = "1,1,1,0,0,0,0,0,0"
        const val IN_PROGRESS_STATE = "1,0,-1,0,0,0,0,0,1"
    }

    @BeforeEach
    fun setup() {
        gameState = GameState()
    }

    @Test
    fun `getNextState should return gameStateResponse with isGameOver as true`() {
        gameState.setGameStatus(NO_WINNER_END_STATE)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = NO_WINNER_END_STATE, winner = 0, gameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return gameStateResponse with correct winner and gameOver as true`() {
        gameState.setGameStatus(WINNER_WITH_NOT_FULL_BOARD)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = WINNER_WITH_NOT_FULL_BOARD, winner = 1, gameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return gameStatus if game is in progress`() {
        gameState.setGameStatus(IN_PROGRESS_STATE)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = IN_PROGRESS_STATE, winner = 0, gameOver = false)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }
}