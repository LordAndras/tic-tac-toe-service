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

    fun addSession(session: WebSocketSession, player: Player) {
        this.sessions[session] = player
    }

    fun removeSession(session: WebSocketSession) {
        this.sessions.remove(session)
    }

    fun setPlayerName(session: WebSocketSession, name: String) {
        val player = getPlayerOfSession(session)
        player.name = name
    }

    fun createGameSession(session: WebSocketSession, systemMessage: SystemMessage) {
        val player = getPlayerOfSession(session)
        val session2 = getSessionFromId(systemMessage.value!!)
        val player2 = getPlayerOfSession(session2)
        player.inGame = true
        player2.inGame = true
        this.gameSessions.add(GameSession(player, player2))
    }

    fun getCurrentGameNumber(): Int {
        return this.gameSessions.size
    }

    private fun getSessionFromId(sessionId: String): WebSocketSession {
        val session = this.sessions.keys.find { it.id == sessionId }
        if (session != null) {
            return session
        } else {
            throw SessionNotFoundException()
        }
    }

    private fun getPlayerOfSession(session: WebSocketSession): Player {
        val player = this.sessions[session]
        if (player != null) {
            return player
        } else {
            throw (PlayerNotFoundException())
        }
    }
}