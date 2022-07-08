package com.example.game

import com.example.game.state.GameState
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameStateTest {
    private lateinit var gameState: GameState

    @BeforeEach
    fun setup() {
        gameState = GameState()
        gameState.setGameStatus("1,-1,1,-1,1,-1,1,-1,1")
    }

    @Test
    fun `reset should set state to all 0`() {
        val expected = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

        gameState.resetStatus()

        gameState.getGameStatus() shouldBe expected
    }

    @Test
    fun `setGameStatus should create the correct Int list from input params`() {

        val expected = mutableListOf(1, -1, 1, -1, 1, -1, 1, -1, 1)

        gameState.getGameStatus() shouldBe expected
    }

    @Test
    fun `getGameStatusString should return state as a correct string`() {

        gameState.resetStatus()

        val result = gameState.getGameStatusString()

        val expected = "0,0,0,0,0,0,0,0,0"

        result shouldBe expected
    }

    @Test
    fun `isGameEnd should return true at game over`() {
        gameState.isGameEnd() shouldBe true
    }

    @Test
    fun `isGameEnd should return false if more steps available`() {
        gameState.resetStatus()
        gameState.isGameEnd() shouldBe false
    }
}