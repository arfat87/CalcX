package com.calcx.calculator.engine

class Parser(private val tokens: List<Token>) {
    private var current = 0

    fun parse(): AstNode {
        if (tokens.isEmpty() || match(TokenType.EOF)) {
            throw CalculationError.InvalidSyntax
        }
        val node = parseExpression()
        if (!match(TokenType.EOF)) {
            throw CalculationError.InvalidSyntax
        }
        return node
    }

    private fun parseExpression(): AstNode {
        return parseAdditionOrSubtraction()
    }

    private fun parseAdditionOrSubtraction(): AstNode {
        var expr = parseMultiplicationOrDivision()

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            val op = previous().type
            val right = parseMultiplicationOrDivision()
            expr = AstNode.BinaryOp(expr, op, right)
        }

        return expr
    }

    private fun parseMultiplicationOrDivision(): AstNode {
        var expr = parseExponentiation()

        while (match(TokenType.MULTIPLY, TokenType.DIVIDE, TokenType.MODULO)) {
            val op = previous().type
            val right = parseExponentiation()
            expr = AstNode.BinaryOp(expr, op, right)
        }

        return expr
    }

    // Exponentiation is right-associative: 2^3^2 = 2^(3^2) = 512
    private fun parseExponentiation(): AstNode {
        val expr = parseUnary()

        if (match(TokenType.POWER)) {
            val op = previous().type
            val right = parseExponentiation()
            return AstNode.BinaryOp(expr, op, right)
        }

        return expr
    }

    private fun parseUnary(): AstNode {
        if (match(TokenType.PLUS, TokenType.MINUS)) {
            val op = previous().type
            val operand = parseUnary()
            return AstNode.UnaryOp(op, operand)
        }

        return parsePostfix()
    }

    private fun parsePostfix(): AstNode {
        var expr = parsePrimary()

        while (match(TokenType.FACTORIAL, TokenType.PERCENT)) {
            val op = previous().type
            expr = AstNode.PostfixOp(expr, op)
        }

        return expr
    }

    private fun parsePrimary(): AstNode {
        if (match(TokenType.NUMBER)) {
            val token = previous()
            return AstNode.Number(token.value ?: token.text.toDouble())
        }

        if (match(TokenType.CONSTANT)) {
            val token = previous()
            return AstNode.Constant(token.text, token.value ?: 0.0)
        }

        if (match(TokenType.FUNCTION)) {
            val funcName = previous().text
            val args = mutableListOf<AstNode>()

            if (match(TokenType.LPAREN)) {
                if (!check(TokenType.RPAREN)) {
                    do {
                        args.add(parseExpression())
                    } while (match(TokenType.COMMA))
                }
                consume(TokenType.RPAREN, "Expect ')' after arguments.")
            } else {
                // Support unparenthesized single argument e.g. √144 or sin 30
                args.add(parsePrimary())
            }

            return AstNode.FunctionCall(funcName, args)
        }

        if (match(TokenType.LPAREN)) {
            val expr = parseExpression()
            consume(TokenType.RPAREN, "Expect ')' after expression.")
            return expr
        }

        throw CalculationError.InvalidSyntax
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        if (isAtEnd()) return type == TokenType.EOF
        return peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return current >= tokens.size || peek().type == TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        if (type == TokenType.RPAREN) {
            throw CalculationError.MismatchedParentheses
        }
        throw CalculationError.InvalidSyntax
    }
}
