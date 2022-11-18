package com.example.game.message.system

import com.example.game.message.system.handler.InviteHandler
import com.example.game.message.system.handler.NameHandler
import com.example.game.message.system.handler.NewGameHandler
import com.example.game.message.system.handler.PlayersHandler
import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class SystemMessageHandlerProvider(private val sessionHandler: SessionHandler, private val newGameService: NewGameService, private val objectMapper: ObjectMapper) {
    private var nameHandler: NameHandler? = null
    private var playersHandler: PlayersHandler? = null
    private var newGameHandler: NewGameHandler? = null
    private var inviteHandler: InviteHandler? = null

    fun provide(systemMessage: SystemMessage): SystemMessageHandler {
       return when (systemMessage.key) {
            "name" -> {
                if (nameHandler == null) {
                    this.nameHandler = NameHandler(sessionHandler, objectMapper)
                    this.nameHandler!!
                } else {
                    this.nameHandler!!
                }
            }

            "players" -> {
                if (playersHandler == null) {
                    this.playersHandler = PlayersHandler(sessionHandler, objectMapper)
                    this.playersHandler!!
                } else {
                    this.playersHandler!!
                }
            }

            "invite" -> {
                if (inviteHandler == null) {
                    this.inviteHandler = InviteHandler(sessionHandler, objectMapper)
                    this.inviteHandler!!
                } else {
                    this.inviteHandler!!
                }
            }

            "newGame" -> {
                if (newGameHandler == null) {
                    this.newGameHandler = NewGameHandler(newGameService, objectMapper)
                    this.newGameHandler!!
                } else {
                    this.newGameHandler!!
                }
            }

            else -> {
                throw InvalidInputException()
            }
        }
    }
}