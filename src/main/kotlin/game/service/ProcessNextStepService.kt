package game.service

import game.logic.GameLogic
import game.model.GameStateResponse
import game.state.GameState
import org.springframework.stereotype.Service

@Service
class ProcessNextStepService(private val gameState: GameState, private val gameLogic: GameLogic) {

    fun processNextStep(nextStep: String): GameStateResponse {
        gameState.setGameStatus(nextStep)
        return gameLogic.getNextState()
    }


}