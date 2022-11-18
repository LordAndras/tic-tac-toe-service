package com.example.game.message.system

import com.example.game.message.system.handler.InviteHandler
import com.example.game.message.system.handler.NameHandler
import com.example.game.model.SocketMessagePayload
import com.example.game.model.SystemMessage
import com.example.game.service.NewGameService
import com.example.game.session.SessionHandler
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Service
class SystemMessageService(
    private val sessionHandler: SessionHandler,
    private val nameHandler: NameHandler,
    private val inviteHandler: InviteHandler,
    private val newGameService: NewGameService,
    private val objectMapper: ObjectMapper
) {
    private companion object {
        const val INVALID_INPUT_ERROR = "Invalid input"
    }

    fun handleMessage(session: WebSocketSession, systemMessage: SystemMessage): TextMessage {
        return try {
            when (systemMessage.key) {
                "name" -> {
                    nameHandler.handle(session, systemMessage)
                }

                "players" -> {
                    val json = objectMapper.writeValueAsString(createPlayersPayload())
                    TextMessage(json)
                }

                "invite" -> {
                    inviteHandler.handle(session, systemMessage)
                }

                "newGame" -> {
                    val json = objectMapper.writeValueAsString(createNewGamePayload())
                    TextMessage(json)
                }

                else -> {
                    TextMessage(objectMapper.writeValueAsString(createErrorPayload(INVALID_INPUT_ERROR)))
                }
            }
        } catch (exception: Exception) {
            TextMessage(objectMapper.writeValueAsString(createErrorPayload(exception.message ?: "Error occurred")))
        }
    }

    private fun createErrorPayload(message: String): SocketMessagePayload {
        val errorSystemMessage = SystemMessage("error", message)
        return SocketMessagePayload(isSysMessage = true, systemMessage = errorSystemMessage)
    }

    private fun createPlayersPayload(): SocketMessagePayload {
        val players = objectMapper.writeValueAsString(sessionHandler.getSessionsWithPlayers().values)
        val playerMessage = SystemMessage("success", players)
        return SocketMessagePayload(isSysMessage = true, systemMessage = playerMessage)
    }

    private fun createNewGamePayload(): SocketMessagePayload {
        return SocketMessagePayload(isSysMessage = true, gameStateResponse = newGameService.newGame())
    }
}