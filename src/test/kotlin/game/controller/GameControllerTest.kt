package game.controller

import game.service.NewGameService
import game.service.ProcessNextStepService
import game.service.ValidateStepService
import io.kotest.matchers.ints.exactly
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameControllerTest {
    private lateinit var mockNewGameService: NewGameService
    private lateinit var mockProcessNextStepService: ProcessNextStepService
    private lateinit var validateStepService: ValidateStepService
    private lateinit var gameController: GameController

    @BeforeEach
    fun setup() {
        mockNewGameService = mockk(relaxed = true)
        mockProcessNextStepService = mockk(relaxed = true)
        validateStepService = ValidateStepService()
        gameController = GameController(mockNewGameService, mockProcessNextStepService, validateStepService)
    }

    @Test
    fun `newGame should call initialize on newGame service`() {

        gameController.newGame()

        verify { mockNewGameService.newGame() }
    }

    @Test
    fun `nextStep should call processNextStep with valid string`() {
        val testString = "1,1,0,0,-1,0,1,0,1"
        gameController.nextStep(testString)

        verify { mockProcessNextStepService.processNextStep(testString) }
    }

    @Test
    fun `nextStep should not call processNextStep with invalid string`() {
        val testString = "test"
        gameController.nextStep(testString)

        verify(exactly = 0) { mockProcessNextStepService.processNextStep(testString) }
    }
}