package com.calcx.calculator.engine

class Lexer(private val input: String) {
    private var pos = 0

    fun tokenize(): List<Token> {
        val rawTokens = mutableListOf<Token>()

        while (pos < input.length) {
            val c = input[pos]

            when {
                c.isWhitespace() -> {
                    pos++
                }
                c.isDigit() || (c == '.' && peekNextIsDigit()) -> {
                    rawTokens.add(readNumber())
                }
                c == '+' -> {
                    rawTokens.add(Token(TokenType.PLUS, "+", position = pos++))
                }
                c == '-' || c == '−' -> {
                    rawTokens.add(Token(TokenType.MINUS, "-", position = pos++))
                }
                c == '*' || c == '×' -> {
                    rawTokens.add(Token(TokenType.MULTIPLY, "×", position = pos++))
                }
                c == '/' || c == '÷' -> {
                    rawTokens.add(Token(TokenType.DIVIDE, "÷", position = pos++))
                }
                c == '^' -> {
                    rawTokens.add(Token(TokenType.POWER, "^", position = pos++))
                }
                c == '%' -> {
                    rawTokens.add(Token(TokenType.PERCENT, "%", position = pos++))
                }
                c == '!' -> {
                    rawTokens.add(Token(TokenType.FACTORIAL, "!", position = pos++))
                }
                c == '(' -> {
                    rawTokens.add(Token(TokenType.LPAREN, "(", position = pos++))
                }
                c == ')' -> {
                    rawTokens.add(Token(TokenType.RPAREN, ")", position = pos++))
                }
                c == ',' -> {
                    rawTokens.add(Token(TokenType.COMMA, ",", position = pos++))
                }
                c == 'π' -> {
                    rawTokens.add(Token(TokenType.CONSTANT, "π", value = MathConstants.PI, position = pos++))
                }
                c == '√' -> {
                    rawTokens.add(Token(TokenType.FUNCTION, "sqrt", position = pos++))
                }
                c == '∛' -> {
                    rawTokens.add(Token(TokenType.FUNCTION, "cbrt", position = pos++))
                }
                c.isLetter() -> {
                    rawTokens.add(readWord())
                }
                else -> {
                    throw CalculationError.InvalidSyntax
                }
            }
        }

        rawTokens.add(Token(TokenType.EOF, "", position = pos))

        // Inject implicit multiplication where unambiguous:
        // Examples: 2(3) -> 2 * (3), (2)(3) -> (2) * (3), 2π -> 2 * π, 5sin(30) -> 5 * sin(30)
        return insertImplicitMultiplication(rawTokens)
    }

    private fun peekNextIsDigit(): Boolean {
        return pos + 1 < input.length && input[pos + 1].isDigit()
    }

    private fun readNumber(): Token {
        val start = pos
        var hasDecimal = false
        var hasExponent = false

        while (pos < input.length) {
            val c = input[pos]
            if (c.isDigit()) {
                pos++
            } else if (c == '.' && !hasDecimal && !hasExponent) {
                hasDecimal = true
                pos++
            } else if ((c == 'e' || c == 'E') && !hasExponent) {
                hasExponent = true
                pos++
                if (pos < input.length && (input[pos] == '+' || input[pos] == '-')) {
                    pos++
                }
            } else {
                break
            }
        }

        val text = input.substring(start, pos)
        val value = text.toDoubleOrNull() ?: throw CalculationError.InvalidSyntax
        return Token(TokenType.NUMBER, text, value = value, position = start)
    }

    private fun readWord(): Token {
        val start = pos
        while (pos < input.length && (input[pos].isLetter() || input[pos].isDigit())) {
            pos++
        }
        val text = input.substring(start, pos)
        val lower = text.lowercase()

        return when (lower) {
            "pi" -> Token(TokenType.CONSTANT, "π", value = MathConstants.PI, position = start)
            "e" -> Token(TokenType.CONSTANT, "e", value = MathConstants.E, position = start)
            "phi" -> Token(TokenType.CONSTANT, "φ", value = MathConstants.GOLDEN_RATIO, position = start)
            "mod" -> Token(TokenType.MODULO, "mod", position = start)
            else -> Token(TokenType.FUNCTION, lower, position = start)
        }
    }

    private fun insertImplicitMultiplication(tokens: List<Token>): List<Token> {
        val result = mutableListOf<Token>()

        for (i in tokens.indices) {
            result.add(tokens[i])
            if (i < tokens.size - 1) {
                val current = tokens[i]
                val next = tokens[i + 1]

                val currentCanPrecedeMultiply = current.type == TokenType.NUMBER ||
                        current.type == TokenType.CONSTANT ||
                        current.type == TokenType.RPAREN ||
                        current.type == TokenType.PERCENT ||
                        current.type == TokenType.FACTORIAL

                val nextCanFollowMultiply = next.type == TokenType.LPAREN ||
                        next.type == TokenType.CONSTANT ||
                        next.type == TokenType.FUNCTION ||
                        (current.type == TokenType.RPAREN && next.type == TokenType.NUMBER)

                if (currentCanPrecedeMultiply && nextCanFollowMultiply) {
                    result.add(Token(TokenType.MULTIPLY, "×", position = current.position))
                }
            }
        }

        return result
    }
}
