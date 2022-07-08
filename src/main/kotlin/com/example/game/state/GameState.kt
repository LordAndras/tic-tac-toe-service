package com.example.game.state

import org.springframework.stereotype.Component

@Component
class GameState {
    private var state: MutableList<Int> = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)

    fun getGameStatus(): MutableList<Int> {
        return this.state
    }

    fun getGameStatusString(): String {
        return this.state.joinToString(separator = ",")
    }

    fun setGameStatus(newStatus: String) {
        this.state = newStatus.split(",").map { number -> number.toInt() }.toMutableList()
    }

    fun resetStatus() {
        this.state = mutableListOf(0, 0, 0, 0, 0, 0, 0, 0, 0)
    }

    fun isGameEnd() : Boolean {
        return !state.contains(0)
    }
}