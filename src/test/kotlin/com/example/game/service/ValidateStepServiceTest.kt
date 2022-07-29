package com.example.game.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ValidateStepServiceTest {
    private lateinit var validateStepService: ValidateStepService

    @BeforeEach
    fun setup() {
        validateStepService = ValidateStepService()
    }

    @Test
    fun`validate should return false if string does not have exactly 9 parts`() {
        val testStep = "234,2,345,56453,-1,0,21,6,99,32"

        val result = validateStepService.validate(testStep)

        result shouldBe false
    }

    @Test
    fun`validate should return false if string contains values other than -1, 0, or 1 `() {
        val testStep = "1,1,0,0,-1,0,1,0,99"

        val result = validateStepService.validate(testStep)

        result shouldBe false
    }

    @Test
    fun`validate should return true if string only contains values of -1, 0, or 1 `() {
        val testStep = "1,1,0,0,-1,0,1,0,1"

        val result = validateStepService.validate(testStep)

        result shouldBe true
    }

    @Test
    fun`validate should return false if string contains non-numeric values`() {
        val testStep = "false,1,0,0,-1,test,1,0,1"

        val result = validateStepService.validate(testStep)

        result shouldBe false
    }
}