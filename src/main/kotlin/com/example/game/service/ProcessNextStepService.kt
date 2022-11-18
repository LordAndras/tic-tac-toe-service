package com.example.game.service

import com.example.game.logic.GameLogic
import com.example.game.model.GameStateResponse
import com.example.game.state.GameState
import org.springframework.stereotype.Service

@Service
class ProcessNextStepService(private val gameState: GameState, private val gameLogic: GameLogic) {

    fun nextStep(nextStep: String): GameStateResponse {
        gameState.setGameStatus(nextStep)
        return gameLogic.getGameStateResponse()
    }


}