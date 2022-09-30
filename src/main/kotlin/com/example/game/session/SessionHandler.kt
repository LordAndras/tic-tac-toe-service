package com.example.game.session

import com.example.game.model.GameSession
import com.example.game.model.Player
import com.example.game.model.SystemMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class SessionHandler {
    private val sessions: MutableMap<WebSocketSession, Player> = mutableMapOf()
    private val gameSessions: MutableList<GameSession> = mutableListOf()

    fun getSessions(): MutableMap<WebSocketSession, Player> {
        return this.sessions
    }

    fun getSessionFromId(sessionId: String): WebSocketSession {
        val session = this.sessions.keys.find { it.id == sessionId }
        if (session != null) {
            return session
        } else {
            throw SessionNotFoundException()
        }
    }

    fun getPlayerOfSession(session: WebSocketSession): Player {
        val player = this.sessions[session]
        if (player != null) {
            return player
        } else {
            throw (PlayerNotFoundException())
        }
    }

    fun addSession(session: WebSocketSession, player: Player) {
        this.sessions[session] = player
    }

    fun removeSession(session: WebSocketSession) {
        this.sessions.remove(session)
    }

    fun setName(session: WebSocketSession, name: String) {
        this.sessions[session]?.name = name
    }

    fun createGameSession(session: WebSocketSession, systemMessage: SystemMessage) {
        val playerOne = getPlayerOfSession(session)
        val sessionTwo = getSessionFromId(systemMessage.value!!)
        val playerTwo = getPlayerOfSession(sessionTwo)
        playerOne.inGame = true
        playerTwo.inGame = true
        this.gameSessions.add(GameSession(playerOne, playerTwo))
    }
}