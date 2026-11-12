package com.calcx.calculator.domain.advancedmath

import kotlin.math.*

object AdvancedMathEngine {

    // --- 11.1 STATISTICS ---
    data class StatisticsResult(
        val count: Int,
        val sum: Double,
        val mean: Double,
        val median: Double,
        val modes: List<Double>,
        val min: Double,
        val max: Double,
        val range: Double,
        val sampleVariance: Double,
        val sampleStdDev: Double,
        val populationVariance: Double,
        val populationStdDev: Double,
        val q1: Double,
        val q3: Double
    )

    fun calculateStatistics(numbers: List<Double>): StatisticsResult {
        if (numbers.isEmpty()) {
            throw IllegalArgumentException("Dataset must not be empty")
        }

        val sorted = numbers.sorted()
        val n = sorted.size
        val sum = sorted.sum()
        val mean = sum / n

        val median = if (n % 2 == 1) {
            sorted[n / 2]
        } else {
            (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0
        }

        // Mode
        val frequencyMap = sorted.groupingBy { it }.eachCount()
        val maxFreq = frequencyMap.values.maxOrNull() ?: 1
        val modes = if (maxFreq > 1) {
            frequencyMap.filterValues { it == maxFreq }.keys.toList()
        } else {
            emptyList()
        }

        val min = sorted.first()
        val max = sorted.last()
        val range = max - min

        val sumSqDiff = sorted.sumOf { (it - mean).pow(2) }
        val popVar = sumSqDiff / n
        val popStdDev = sqrt(popVar)

        val sampleVar = if (n > 1) sumSqDiff / (n - 1) else 0.0
        val sampleStdDev = sqrt(sampleVar)

        val q1 = percentile(sorted, 25.0)
        val q3 = percentile(sorted, 75.0)

        return StatisticsResult(
            count = n,
            sum = sum,
            mean = mean,
            median = median,
            modes = modes,
            min = min,
            max = max,
            range = range,
            sampleVariance = sampleVar,
            sampleStdDev = sampleStdDev,
            populationVariance = popVar,
            populationStdDev = popStdDev,
            q1 = q1,
            q3 = q3
        )
    }

    private fun percentile(sorted: List<Double>, p: Double): Double {
        if (sorted.size == 1) return sorted[0]
        val rank = (p / 100.0) * (sorted.size - 1)
        val lower = floor(rank).toInt()
        val upper = ceil(rank).toInt()
        val weight = rank - lower
        return sorted[lower] * (1.0 - weight) + sorted[upper] * weight
    }

    // --- 11.2 FRACTIONS ---
    data class Fraction(val numerator: Long, val denominator: Long) {
        init {
            require(denominator != 0L) { "Denominator cannot be zero" }
        }

        fun simplify(): Fraction {
            val gcdVal = gcd(abs(numerator), abs(denominator))
            val sign = if (denominator < 0) -1L else 1L
            return Fraction((numerator / gcdVal) * sign, (denominator / gcdVal) * sign)
        }

        operator fun plus(other: Fraction): Fraction {
            val num = this.numerator * other.denominator + other.numerator * this.denominator
            val den = this.denominator * other.denominator
            return Fraction(num, den).simplify()
        }

        operator fun minus(other: Fraction): Fraction {
            val num = this.numerator * other.denominator - other.numerator * this.denominator
            val den = this.denominator * other.denominator
            return Fraction(num, den).simplify()
        }

        operator fun times(other: Fraction): Fraction {
            val num = this.numerator * other.numerator
            val den = this.denominator * other.denominator
            return Fraction(num, den).simplify()
        }

        operator fun div(other: Fraction): Fraction {
            require(other.numerator != 0L) { "Cannot divide by zero fraction" }
            val num = this.numerator * other.denominator
            val den = this.denominator * other.numerator
            return Fraction(num, den).simplify()
        }

        fun toMixedString(): String {
            val s = simplify()
            val whole = s.numerator / s.denominator
            val remainder = abs(s.numerator % s.denominator)
            return when {
                remainder == 0L -> whole.toString()
                whole == 0L -> "${s.numerator}/${s.denominator}"
                else -> "$whole ${remainder}/${s.denominator}"
            }
        }

        override fun toString(): String = "${simplify().numerator}/${simplify().denominator}"
    }

    private fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    fun decimalToFraction(value: Double, tolerance: Double = 1e-6): Fraction {
        if (value == floor(value)) return Fraction(value.toLong(), 1L)

        var h1 = 1L
        var h2 = 0L
        var k1 = 0L
        var k2 = 1L
        var b = value

        do {
            val a = floor(b).toLong()
            var aux = h1
            h1 = a * h1 + h2
            h2 = aux
            aux = k1
            k1 = a * k1 + k2
            k2 = aux
            b = 1.0 / (b - a)
        } while (abs(value - h1.toDouble() / k1.toDouble()) > value * tolerance && h1 < 1e9 && k1 < 1e9)

        return Fraction(h1, k1).simplify()
    }

    // --- 11.3 MATRIX CALCULATIONS ---
    data class Matrix(val rows: Int, val cols: Int, val data: List<List<Double>>) {
        init {
            require(data.size == rows && data.all { it.size == cols }) {
                "Invalid matrix data dimensions"
            }
        }

        fun transpose(): Matrix {
            val transposed = (0 until cols).map { c ->
                (0 until rows).map { r -> data[r][c] }
            }
            return Matrix(cols, rows, transposed)
        }

        operator fun plus(other: Matrix): Matrix {
            require(rows == other.rows && cols == other.cols) { "Matrices must have identical dimensions for addition" }
            val res = (0 until rows).map { r ->
                (0 until cols).map { c -> data[r][c] + other.data[r][c] }
            }
            return Matrix(rows, cols, res)
        }

        operator fun minus(other: Matrix): Matrix {
            require(rows == other.rows && cols == other.cols) { "Matrices must have identical dimensions for subtraction" }
            val res = (0 until rows).map { r ->
                (0 until cols).map { c -> data[r][c] - other.data[r][c] }
            }
            return Matrix(rows, cols, res)
        }

        operator fun times(other: Matrix): Matrix {
            require(cols == other.rows) { "Matrix A cols ($cols) must match Matrix B rows (${other.rows})" }
            val res = (0 until rows).map { r ->
                (0 until other.cols).map { c ->
                    (0 until cols).sumOf { k -> data[r][k] * other.data[k][c] }
                }
            }
            return Matrix(rows, other.cols, res)
        }

        fun determinant(): Double {
            require(rows == cols) { "Determinant only defined for square matrices" }
            return detRecursive(data)
        }

        private fun detRecursive(m: List<List<Double>>): Double {
            val n = m.size
            if (n == 1) return m[0][0]
            if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0]

            var det = 0.0
            for (c in 0 until n) {
                val sub = (1 until n).map { r ->
                    m[r].filterIndexed { idx, _ -> idx != c }
                }
                val sign = if (c % 2 == 0) 1.0 else -1.0
                det += sign * m[0][c] * detRecursive(sub)
            }
            return det
        }

        fun inverse(): Matrix {
            require(rows == cols) { "Inverse only defined for square matrices" }
            val det = determinant()
            if (abs(det) < 1e-12) {
                throw ArithmeticException("Matrix is singular (determinant = 0), cannot invert")
            }

            val n = rows
            if (n == 1) return Matrix(1, 1, listOf(listOf(1.0 / data[0][0])))

            // Adjugate matrix / det
            val adj = (0 until n).map { r ->
                (0 until n).map { c ->
                    // Cofactor at (c, r) for adjugate transpose
                    val sub = (0 until n).filter { it != c }.map { subR ->
                        data[subR].filterIndexed { idx, _ -> idx != r }
                    }
                    val sign = if ((r + c) % 2 == 0) 1.0 else -1.0
                    (sign * detRecursive(sub)) / det
                }
            }
            return Matrix(n, n, adj)
        }
    }

    // --- 11.4 EQUATION SOLVERS ---
    sealed class LinearSolution {
        data class Single(val x: Double) : LinearSolution()
        object Infinite : LinearSolution()
        object None : LinearSolution()
    }

    fun solveLinear(a: Double, b: Double): LinearSolution {
        // ax + b = 0
        return when {
            a != 0.0 -> LinearSolution.Single(-b / a)
            b == 0.0 -> LinearSolution.Infinite
            else -> LinearSolution.None
        }
    }

    sealed class QuadraticSolution {
        data class TwoReal(val x1: Double, val x2: Double) : QuadraticSolution()
        data class OneReal(val x: Double) : QuadraticSolution()
        data class Complex(val real: Double, val imag: Double) : QuadraticSolution()
        object LinearFallback : QuadraticSolution()
    }

    fun solveQuadratic(a: Double, b: Double, c: Double): QuadraticSolution {
        if (a == 0.0) return QuadraticSolution.LinearFallback

        val d = b * b - 4 * a * c
        return when {
            d > 1e-12 -> {
                val x1 = (-b + sqrt(d)) / (2 * a)
                val x2 = (-b - sqrt(d)) / (2 * a)
                QuadraticSolution.TwoReal(x1, x2)
            }
            abs(d) <= 1e-12 -> {
                val x = -b / (2 * a)
                QuadraticSolution.OneReal(x)
            }
            else -> {
                val real = -b / (2 * a)
                val imag = sqrt(-d) / (2 * a)
                QuadraticSolution.Complex(real, abs(imag))
            }
        }
    }

    data class System2x2Solution(val x: Double, val y: Double)

    fun solveSystem2x2(
        a1: Double, b1: Double, c1: Double,
        a2: Double, b2: Double, c2: Double
    ): System2x2Solution? {
        // a1*x + b1*y = c1
        // a2*x + b2*y = c2
        val d = a1 * b2 - a2 * b1
        if (abs(d) < 1e-12) return null // Parallel or coincident
        val dx = c1 * b2 - c2 * b1
        val dy = a1 * c2 - a2 * c1
        return System2x2Solution(dx / d, dy / d)
    }
}
