package com.example.game.session

import com.example.game.model.Player
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class SessionHandler {
    private val sessions: MutableMap<WebSocketSession, Player> = mutableMapOf()

    fun getSessions() : List<WebSocketSession> {
        return this.sessions.keys.toList()
    }

    fun addSession(session: WebSocketSession, player: Player) {
        this.sessions[session] = player
    }

    fun removeSession(session: WebSocketSession) {
        this.sessions.remove(session)
    }
}