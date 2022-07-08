package game.controller

import game.model.GameStateResponse
import game.service.NewGameService
import game.service.ProcessNextStepService
import game.service.ValidateStepService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    private val newGameService: NewGameService,
    private val processNextStepService: ProcessNextStepService,
    private val validateStepService: ValidateStepService
) {
    @GetMapping("newGame")
    fun newGame(): ResponseEntity<GameStateResponse> {
        return ResponseEntity.ok(newGameService.newGame())
    }

    @PostMapping("nextStep")
    fun nextStep(
        @RequestBody nextStep: String
    ): ResponseEntity<GameStateResponse> {
        return if (validateStepService.validate(nextStep)) {
            ResponseEntity.ok(processNextStepService.processNextStep(nextStep))
        } else {
            ResponseEntity.badRequest().build()
        }
    }
}
