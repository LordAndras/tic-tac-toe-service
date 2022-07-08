package game.model

data class GameStateResponse(
    val gameState: String = "",
    val winner: Int = 0,
    val isGameOver: Boolean = false
)