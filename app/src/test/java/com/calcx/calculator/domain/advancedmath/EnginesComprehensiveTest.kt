package com.calcx.calculator.domain.advancedmath

import com.calcx.calculator.domain.converter.UnitCategory
import com.calcx.calculator.domain.converter.UnitConverterEngine
import com.calcx.calculator.domain.programmer.BitWidth
import com.calcx.calculator.domain.programmer.NumberBase
import com.calcx.calculator.domain.programmer.ProgrammerEngine
import com.calcx.calculator.domain.programmer.ProgrammerState
import org.junit.Assert.*
import org.junit.Test

class EnginesComprehensiveTest {

    // --- Statistics Tests ---
    @Test
    fun testStatistics() {
        val data = listOf(2.0, 4.0, 4.0, 4.0, 5.0, 5.0, 7.0, 9.0)
        val stats = AdvancedMathEngine.calculateStatistics(data)
        assertEquals(8, stats.count)
        assertEquals(40.0, stats.sum, 1e-6)
        assertEquals(5.0, stats.mean, 1e-6)
        assertEquals(4.5, stats.median, 1e-6)
        assertEquals(listOf(4.0), stats.modes)
        assertEquals(2.0, stats.min, 1e-6)
        assertEquals(9.0, stats.max, 1e-6)
        assertEquals(7.0, stats.range, 1e-6)
        assertEquals(4.0, stats.populationVariance, 1e-6)
        assertEquals(2.0, stats.populationStdDev, 1e-6)
    }

    // --- Fractions Tests ---
    @Test
    fun testFractionsArithmetic() {
        val f1 = AdvancedMathEngine.Fraction(1, 2)
        val f2 = AdvancedMathEngine.Fraction(1, 3)

        val sum = f1 + f2
        assertEquals(5L, sum.numerator)
        assertEquals(6L, sum.denominator)

        val diff = f1 - f2
        assertEquals(1L, diff.numerator)
        assertEquals(6L, diff.denominator)

        val mult = f1 * f2
        assertEquals(1L, mult.numerator)
        assertEquals(6L, mult.denominator)

        val div = f1 / f2
        assertEquals(3L, div.numerator)
        assertEquals(2L, div.denominator)
        assertEquals("1 1/2", div.toMixedString())
    }

    @Test
    fun testDecimalToFraction() {
        val f = AdvancedMathEngine.decimalToFraction(0.75)
        assertEquals(3L, f.numerator)
        assertEquals(4L, f.denominator)

        val f2 = AdvancedMathEngine.decimalToFraction(0.3333333333)
        assertEquals(1L, f2.numerator)
        assertEquals(3L, f2.denominator)
    }

    // --- Matrix Tests ---
    @Test
    fun testMatrixDeterminantAndInverse() {
        // [ [1, 2], [3, 4] ]
        val m = AdvancedMathEngine.Matrix(2, 2, listOf(listOf(1.0, 2.0), listOf(3.0, 4.0)))
        assertEquals(-2.0, m.determinant(), 1e-6)

        val inv = m.inverse()
        val identity = m * inv
        assertEquals(1.0, identity.data[0][0], 1e-6)
        assertEquals(0.0, identity.data[0][1], 1e-6)
        assertEquals(0.0, identity.data[1][0], 1e-6)
        assertEquals(1.0, identity.data[1][1], 1e-6)
    }

    // --- Equation Solver Tests ---
    @Test
    fun testEquationSolvers() {
        // 2x + 6 = 0 -> x = -3
        val lin = AdvancedMathEngine.solveLinear(2.0, 6.0)
        assertTrue(lin is AdvancedMathEngine.LinearSolution.Single)
        assertEquals(-3.0, (lin as AdvancedMathEngine.LinearSolution.Single).x, 1e-6)

        // x^2 - 5x + 6 = 0 -> roots 3 and 2
        val quad = AdvancedMathEngine.solveQuadratic(1.0, -5.0, 6.0)
        assertTrue(quad is AdvancedMathEngine.QuadraticSolution.TwoReal)
        val roots = quad as AdvancedMathEngine.QuadraticSolution.TwoReal
        assertEquals(3.0, roots.x1, 1e-6)
        assertEquals(2.0, roots.x2, 1e-6)

        // 2x + y = 5, x - y = 1 -> x = 2, y = 1
        val sys = AdvancedMathEngine.solveSystem2x2(2.0, 1.0, 5.0, 1.0, -1.0, 1.0)
        assertNotNull(sys)
        assertEquals(2.0, sys!!.x, 1e-6)
        assertEquals(1.0, sys.y, 1e-6)
    }

    // --- Unit Converter Tests ---
    @Test
    fun testUnitConversions() {
        // 1 km = 1000 m
        assertEquals(1000.0, UnitConverterEngine.convert(UnitCategory.LENGTH, "km", "m", 1.0), 1e-6)
        // 1 mile = 1609.344 m
        assertEquals(1609.344, UnitConverterEngine.convert(UnitCategory.LENGTH, "mi", "m", 1.0), 1e-6)
        // 1 kg = 1000 g
        assertEquals(1000.0, UnitConverterEngine.convert(UnitCategory.WEIGHT, "kg", "g", 1.0), 1e-6)
        // 100 C = 212 F
        assertEquals(212.0, UnitConverterEngine.convert(UnitCategory.TEMPERATURE, "c", "f", 100.0), 1e-6)
        // 0 C = 273.15 K
        assertEquals(273.15, UnitConverterEngine.convert(UnitCategory.TEMPERATURE, "c", "k", 0.0), 1e-6)
        // 1 GB = 1024 MB
        assertEquals(1024.0, UnitConverterEngine.convert(UnitCategory.DATA, "gb", "mb", 1.0), 1e-6)
    }

    // --- Programmer Calculator Tests ---
    @Test
    fun testProgrammerOperations() {
        val state = ProgrammerState(rawValue = 255L, bitWidth = BitWidth.BYTE)
        assertEquals("FF", state.toFormattedString(NumberBase.HEX))
        assertEquals("-1", state.toFormattedString(NumberBase.DEC)) // 0xFF as signed byte is -1
        assertEquals("377", state.toFormattedString(NumberBase.OCT))

        // Bitwise AND 0b1100 (12) & 0b1010 (10) = 0b1000 (8)
        assertEquals(8L, ProgrammerEngine.bitwiseAnd(12L, 10L, BitWidth.BYTE))
        // Bitwise OR 0b1100 (12) | 0b1010 (10) = 0b1110 (14)
        assertEquals(14L, ProgrammerEngine.bitwiseOr(12L, 10L, BitWidth.BYTE))
        // Bitwise XOR 0b1100 (12) ^ 0b1010 (10) = 0b0110 (6)
        assertEquals(6L, ProgrammerEngine.bitwiseXor(12L, 10L, BitWidth.BYTE))
    }
}
