package com.calcx.calculator.engine

enum class TokenType {
    NUMBER,
    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    POWER,
    MODULO,
    PERCENT,
    FACTORIAL,
    LPAREN,
    RPAREN,
    COMMA,
    FUNCTION,
    CONSTANT,
    EOF
}

data class Token(
    val type: TokenType,
    val text: String,
    val value: Double? = null,
    val position: Int = 0
)
