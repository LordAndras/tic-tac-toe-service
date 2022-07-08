package com.example.game.service

import com.example.game.model.GameStateResponse
import com.example.game.service.NewGameService
import com.example.game.state.GameState
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class NewGameServiceTest {

    @Test
    fun `newGame should call reset on gameState`() {
        val mockGameState: GameState = mockk(relaxed = true)
        val newGameService = NewGameService(mockGameState)

        newGameService.newGame()

        verify { mockGameState.resetStatus() }
    }

    @Test
    fun `newGame should return correct string`() {
        val gameState = GameState()
        gameState.setGameStatus("1,2,3,4,5,6,7,8,9")
        val newGameService = NewGameService(gameState)

        val expectedResult = GameStateResponse(gameState = "0,0,0,0,0,0,0,0,0", winner = 0, isGameOver = false)

        val result = newGameService.newGame()

        result shouldBe expectedResult
    }
}