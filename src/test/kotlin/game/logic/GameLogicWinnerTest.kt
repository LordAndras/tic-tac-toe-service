package game.logic

import game.model.GameStateResponse
import game.state.GameState
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameLogicWinnerTest {
    private lateinit var gameState: GameState

    @BeforeEach
    fun setup() {
        gameState = GameState()
    }

    @Test
    fun `getNextState should return correct winner on 1st row`() {
        val firstRowWins = "1,1,1,0,0,0,0,0,0"
        gameState.setGameStatus(firstRowWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = firstRowWins, winner = 1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on 2nd row`() {
        val secondRowWins = "1,0,1,-1,-1,-1,0,0,0"
        gameState.setGameStatus(secondRowWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = secondRowWins, winner = -1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on 3rd row`() {
        val thirdRowWins = "1,0,1,0,-1,-1,1,1,1"
        gameState.setGameStatus(thirdRowWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = thirdRowWins, winner = 1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on 1st column`() {
        val firstColumnWins = "1,-1,1,1,-1,-1,1,1,-1"
        gameState.setGameStatus(firstColumnWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = firstColumnWins, winner = 1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on 2nd column`() {
        val secondColumnWins = "1,-1,1,0,-1,-1,0,-1,1"
        gameState.setGameStatus(secondColumnWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = secondColumnWins, winner = -1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on 3rd column`() {
        val thirdColumnWins = "1,-1,1,0,-1,1,0,0,1"
        gameState.setGameStatus(thirdColumnWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = thirdColumnWins, winner = 1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on left-down`() {
        val leftDownWins = "1,-1,1,0,1,-1,1,0,1"
        gameState.setGameStatus(leftDownWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = leftDownWins, winner = 1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }

    @Test
    fun `getNextState should return correct winner on right-down`() {
        val rightDownWins = "1,-1,-1,0,-1,-1,-1,0,1"
        gameState.setGameStatus(rightDownWins)
        val gameLogic = GameLogic(gameState)

        val expected = GameStateResponse(gameState = rightDownWins, winner = -1, isGameOver = true)

        val result = gameLogic.getNextState()

        result shouldBe expected
    }
}