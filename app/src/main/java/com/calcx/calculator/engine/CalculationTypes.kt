package com.calcx.calculator.engine

sealed class CalculationError(message: String) : Exception(message) {
    object DivisionByZero : CalculationError("Cannot divide by zero")
    object DomainError : CalculationError("Math error: outside domain")
    object Overflow : CalculationError("Math error: overflow")
    object InvalidSyntax : CalculationError("Invalid syntax")
    object MismatchedParentheses : CalculationError("Mismatched parentheses")
    object FactorialLimitExceeded : CalculationError("Factorial limit exceeded (max 170)")
    data class UnknownFunction(val name: String) : CalculationError("Unknown function: $name")
    data class GenericError(val err: String) : CalculationError(err)
}

enum class AngleMode {
    DEG,
    RAD
}

object MathConstants {
    const val PI = Math.PI
    const val E = Math.E
    const val GOLDEN_RATIO = 1.618033988749895
    const val SPEED_OF_LIGHT = 299792458.0 // m/s
    const val PLANCK = 6.62607015e-34 // J*s
    const val AVOGADRO = 6.02214076e23 // mol^-1
}
