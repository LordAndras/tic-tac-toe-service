package game.service

import game.model.GameStateResponse
import game.state.GameState
import org.springframework.stereotype.Service

@Service
class NewGameService(private var gameState: GameState) {

    fun newGame(): GameStateResponse {
        gameState.resetStatus()
        return GameStateResponse(gameState.getGameStatusString(), 0, gameState.isGameEnd())
    }
}