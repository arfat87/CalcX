package com.calcx.calculator.engine

import kotlin.math.*

class Evaluator(private val angleMode: AngleMode = AngleMode.DEG) {

    fun evaluate(node: AstNode): Double {
        return when (node) {
            is AstNode.Number -> node.value
            is AstNode.Constant -> node.value
            is AstNode.UnaryOp -> evaluateUnary(node.op, node.operand)
            is AstNode.BinaryOp -> evaluateBinary(node.left, node.op, node.right)
            is AstNode.PostfixOp -> evaluatePostfix(node.operand, node.op)
            is AstNode.FunctionCall -> evaluateFunction(node.name, node.args)
        }
    }

    private fun evaluateUnary(op: TokenType, operand: AstNode): Double {
        val v = evaluate(operand)
        return when (op) {
            TokenType.MINUS -> -v
            TokenType.PLUS -> v
            else -> throw CalculationError.InvalidSyntax
        }
    }

    private fun evaluateBinary(left: AstNode, op: TokenType, right: AstNode): Double {
        val l = evaluate(left)
        val r = evaluate(right)

        return when (op) {
            TokenType.PLUS -> l + r
            TokenType.MINUS -> l - r
            TokenType.MULTIPLY -> l * r
            TokenType.DIVIDE -> {
                if (abs(r) < 1e-15) {
                    throw CalculationError.DivisionByZero
                }
                l / r
            }
            TokenType.POWER -> {
                if (l < 0 && r != floor(r)) {
                    throw CalculationError.DomainError
                }
                val res = l.pow(r)
                if (res.isInfinite() || res.isNaN()) throw CalculationError.DomainError
                res
            }
            TokenType.MODULO -> {
                if (abs(r) < 1e-15) throw CalculationError.DivisionByZero
                l % r
            }
            else -> throw CalculationError.InvalidSyntax
        }
    }

    private fun evaluatePostfix(operand: AstNode, op: TokenType): Double {
        val v = evaluate(operand)
        return when (op) {
            TokenType.PERCENT -> v / 100.0
            TokenType.FACTORIAL -> {
                if (v < 0 || v != floor(v)) throw CalculationError.DomainError
                if (v > 170) throw CalculationError.FactorialLimitExceeded
                factorial(v.toInt())
            }
            else -> throw CalculationError.InvalidSyntax
        }
    }

    private fun evaluateFunction(name: String, args: List<AstNode>): Double {
        val evaluatedArgs = args.map { evaluate(it) }

        return when (name.lowercase()) {
            "sin" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val rad = toRadiansIfNeeded(evaluatedArgs[0])
                cleanTrig(sin(rad), evaluatedArgs[0], isSin = true)
            }
            "cos" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val rad = toRadiansIfNeeded(evaluatedArgs[0])
                cleanTrig(cos(rad), evaluatedArgs[0], isCos = true)
            }
            "tan" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val arg = evaluatedArgs[0]
                if (angleMode == AngleMode.DEG && abs(arg % 180) == 90.0) {
                    throw CalculationError.DomainError
                }
                val rad = toRadiansIfNeeded(arg)
                val res = tan(rad)
                if (abs(res) > 1e14) throw CalculationError.DomainError
                cleanTrig(res, arg, isTan = true)
            }
            "asin" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v < -1.0 || v > 1.0) throw CalculationError.DomainError
                val rad = asin(v)
                fromRadiansIfNeeded(rad)
            }
            "acos" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v < -1.0 || v > 1.0) throw CalculationError.DomainError
                val rad = acos(v)
                fromRadiansIfNeeded(rad)
            }
            "atan" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val rad = atan(evaluatedArgs[0])
                fromRadiansIfNeeded(rad)
            }
            "sinh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                sinh(evaluatedArgs[0])
            }
            "cosh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                cosh(evaluatedArgs[0])
            }
            "tanh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                tanh(evaluatedArgs[0])
            }
            "asinh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                asinh(evaluatedArgs[0])
            }
            "acosh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v < 1.0) throw CalculationError.DomainError
                acosh(v)
            }
            "atanh" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v <= -1.0 || v >= 1.0) throw CalculationError.DomainError
                atanh(v)
            }
            "log", "log10" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v <= 0) throw CalculationError.DomainError
                log10(v)
            }
            "ln" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v <= 0) throw CalculationError.DomainError
                ln(v)
            }
            "log2" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v <= 0) throw CalculationError.DomainError
                log2(v)
            }
            "sqrt" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v < 0) throw CalculationError.DomainError
                sqrt(v)
            }
            "cbrt" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                val v = evaluatedArgs[0]
                if (v < 0) -cbrt(-v) else cbrt(v)
            }
            "abs" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                abs(evaluatedArgs[0])
            }
            "exp" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                exp(evaluatedArgs[0])
            }
            "deg" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                Math.toDegrees(evaluatedArgs[0])
            }
            "rad" -> {
                ensureArgCount(name, evaluatedArgs, 1)
                Math.toRadians(evaluatedArgs[0])
            }
            "npr", "perm" -> {
                ensureArgCount(name, evaluatedArgs, 2)
                val n = evaluatedArgs[0]
                val r = evaluatedArgs[1]
                if (n < 0 || r < 0 || r > n || n != floor(n) || r != floor(r)) {
                    throw CalculationError.DomainError
                }
                permutation(n.toInt(), r.toInt())
            }
            "ncr", "comb" -> {
                ensureArgCount(name, evaluatedArgs, 2)
                val n = evaluatedArgs[0]
                val r = evaluatedArgs[1]
                if (n < 0 || r < 0 || r > n || n != floor(n) || r != floor(r)) {
                    throw CalculationError.DomainError
                }
                combination(n.toInt(), r.toInt())
            }
            else -> throw CalculationError.UnknownFunction(name)
        }
    }

    private fun toRadiansIfNeeded(value: Double): Double {
        return if (angleMode == AngleMode.DEG) Math.toRadians(value) else value
    }

    private fun fromRadiansIfNeeded(value: Double): Double {
        return if (angleMode == AngleMode.DEG) Math.toDegrees(value) else value
    }

    private fun cleanTrig(result: Double, originalArg: Double, isSin: Boolean = false, isCos: Boolean = false, isTan: Boolean = false): Double {
        if (angleMode == AngleMode.DEG) {
            val normalized = ((originalArg % 360) + 360) % 360
            if (isSin && (normalized == 0.0 || normalized == 180.0 || normalized == 360.0)) return 0.0
            if (isSin && normalized == 90.0) return 1.0
            if (isSin && normalized == 270.0) return -1.0
            if (isCos && (normalized == 90.0 || normalized == 270.0)) return 0.0
            if (isCos && (normalized == 0.0 || normalized == 360.0)) return 1.0
            if (isCos && normalized == 180.0) return -1.0
            if (isTan && (normalized == 0.0 || normalized == 180.0 || normalized == 360.0)) return 0.0
            if (isTan && (normalized == 45.0 || normalized == 225.0)) return 1.0
            if (isTan && (normalized == 135.0 || normalized == 315.0)) return -1.0
        }
        return if (abs(result) < 1e-15) 0.0 else result
    }

    private fun ensureArgCount(name: String, args: List<Double>, expected: Int) {
        if (args.size != expected) {
            throw CalculationError.GenericError("Function $name expects $expected argument(s), got ${args.size}")
        }
    }

    private fun factorial(n: Int): Double {
        var res = 1.0
        for (i in 2..n) {
            res *= i
        }
        return res
    }

    private fun permutation(n: Int, r: Int): Double {
        var res = 1.0
        for (i in (n - r + 1)..n) {
            res *= i
        }
        return res
    }

    private fun combination(n: Int, r: Int): Double {
        val k = min(r, n - r)
        var res = 1.0
        for (i in 1..k) {
            res = res * (n - i + 1) / i
        }
        return res
    }
}
