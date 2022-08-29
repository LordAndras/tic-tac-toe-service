package com.example.game.session

import com.example.game.model.Player
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

@Component
class SessionHandler {
    private val sessions: MutableMap<WebSocketSession, Player> = mutableMapOf()

    fun getSessions() : MutableMap<WebSocketSession, Player> {
        return this.sessions
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
}