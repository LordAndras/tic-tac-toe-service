package com.example.game.service

import org.springframework.stereotype.Service

@Service
class ValidateStepService {
    private companion object {
        const val EXPECTED_CONTENT_NUMBER = 9
        val allowedValues = listOf(-1, 0, 1)
    }

    fun validate(nextStep: String): Boolean {
        var hasOnlyNumbers = true
        var hasCorrectContent = false
        var numbersFromNextStep: List<Int>? = null

        val contents = splitStringWithComaSeparatedValues(nextStep)
        val isCorrectSize = isCorrectSize(contents)

        if (isCorrectSize) {
            try {
                numbersFromNextStep = mapNumbers(contents)
            } catch (exception: NumberFormatException) {
                hasOnlyNumbers = false
            }
        }

        if (hasOnlyNumbers) hasCorrectContent = hasCorrectContent(numbersFromNextStep)

        return isCorrectSize && hasOnlyNumbers && hasCorrectContent
    }

    private fun splitStringWithComaSeparatedValues(stringWithComaSeparatedValues: String): List<String> {
        return stringWithComaSeparatedValues.split(",")
    }

    private fun isCorrectSize(list: List<String>): Boolean {
        return list.size == EXPECTED_CONTENT_NUMBER
    }

    private fun mapNumbers(contents: List<String>): List<Int> {
        return contents.map { it.toInt() }
    }

    private fun hasCorrectContent(numbers: List<Int>?): Boolean {
        return if (numbers != null) numbers.filter { allowedValues.contains(it) }.size == EXPECTED_CONTENT_NUMBER else false
    }
}