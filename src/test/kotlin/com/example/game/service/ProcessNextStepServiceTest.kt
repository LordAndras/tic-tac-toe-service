package com.example.game.service

import com.example.game.logic.GameLogic
import com.example.game.state.GameState
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class ProcessNextStepServiceTest {

    @Test
    fun `processNextStep should call setGameState`() {
        val mockGameState: GameState = mockk(relaxed = true)
        val mockGameLogic: GameLogic = mockk(relaxed = true)
        val processNextStepService = ProcessNextStepService(mockGameState, mockGameLogic)
        val testString = "test"

        processNextStepService.processNextStep(testString)

        verify { mockGameState.setGameStatus(testString) }
    }

    @Test
    fun `processNextStep should call getGameStateResponse`() {
        val mockGameState: GameState = mockk(relaxed = true)
        val mockGameLogic: GameLogic = mockk(relaxed = true)
        val processNextStepService = ProcessNextStepService(mockGameState, mockGameLogic)
        val testString = "test"

        processNextStepService.processNextStep(testString)

        verify { mockGameLogic.getGameStateResponse() }
    }
}