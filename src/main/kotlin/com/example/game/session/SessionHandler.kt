package com.example.game.session

import com.example.game.model.GameSession
import com.example.game.model.Player
import com.example.game.model.SystemMessage
import com.example.game.sending.MessageSendingService
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class SessionHandler(private val messageSendingService: MessageSendingService) {
    private val sessions: MutableMap<WebSocketSession, Player> = mutableMapOf()
    private val gameSessions: MutableList<GameSession> = mutableListOf()

    fun getSessionsWithPlayers(): MutableMap<WebSocketSession, Player> {
        return this.sessions
    }

    fun getPlayers(): MutableList<String> {
        val response = mutableListOf<String>()
        this.sessions.values.map { player ->
            player.name?.let { name ->
                response.add(name)
            }
        }
        return response
    }

    fun getSessions(): MutableSet<WebSocketSession> {
        return this.sessions.keys
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
        val session2 = getSessionFromPlayerName(systemMessage.value!!)
        val player2 = getPlayerOfSession(session2)
        player.inGame = true
        player2.inGame = true
        this.gameSessions.add(GameSession(player, player2))
        messageSendingService.sendInvite(session2, player)
    }

    fun getCurrentGameNumber(): Int {
        return this.gameSessions.size
    }

    private fun getSessionFromPlayerName(name: String): WebSocketSession {
        return try {
            this.sessions.filter { entry -> entry.value.name == name }.keys.toList().first()
        } catch (exception: Exception) {
            throw SessionNotFoundException()
        }
    }

    private fun getPlayerOfSession(session: WebSocketSession): Player {
        val player = this.sessions[session]
        if (player != null) {
            return player
        } else {
            throw PlayerNotFoundException()
        }
    }
}