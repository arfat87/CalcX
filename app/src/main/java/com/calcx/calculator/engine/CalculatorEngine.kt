package com.calcx.calculator.engine

class CalculatorMemory {
    private var memoryValue: Double = 0.0
    private var isSet: Boolean = false

    fun clear() {
        memoryValue = 0.0
        isSet = false
    }

    fun recall(): Double {
        return memoryValue
    }

    fun store(value: Double) {
        memoryValue = value
        isSet = true
    }

    fun add(value: Double) {
        memoryValue += value
        isSet = true
    }

    fun subtract(value: Double) {
        memoryValue -= value
        isSet = true
    }

    fun hasMemory(): Boolean = isSet
}

data class CalculationEvaluation(
    val result: Double,
    val formattedResult: String
)

class CalculatorEngine(
    var angleMode: AngleMode = AngleMode.DEG,
    var decimalPrecision: Int = 8,
    var thousandsSeparator: Boolean = true
) {
    val memory = CalculatorMemory()

    fun evaluate(expression: String): Result<CalculationEvaluation> {
        return try {
            val cleaned = NumberFormatter.cleanInput(expression)
            val tokens = Lexer(cleaned).tokenize()
            val ast = Parser(tokens).parse()
            val evaluator = Evaluator(angleMode)
            val result = evaluator.evaluate(ast)

            if (result.isNaN() || result.isInfinite()) {
                Result.failure(CalculationError.DomainError)
            } else {
                val formatted = NumberFormatter.format(
                    value = result,
                    maxDecimals = decimalPrecision,
                    useThousandsSeparator = thousandsSeparator
                )
                Result.success(CalculationEvaluation(result, formatted))
            }
        } catch (e: CalculationError) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(CalculationError.InvalidSyntax)
        }
    }
}
