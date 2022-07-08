package com.example.game

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class TicTacToeGame

fun main(args: Array<String>) {
    runApplication<TicTacToeGame>(*args)
}