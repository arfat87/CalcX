package com.calcx.calculator.engine

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CalculatorEngineTest {

    private lateinit var engine: CalculatorEngine

    @Before
    fun setUp() {
        engine = CalculatorEngine()
    }

    @Test
    fun testBasicArithmetic() {
        assertEquals("4", engine.evaluate("2 + 2").getOrThrow().formattedResult)
        assertEquals("7", engine.evaluate("10 - 3").getOrThrow().formattedResult)
        assertEquals("30", engine.evaluate("5 × 6").getOrThrow().formattedResult)
        assertEquals("25", engine.evaluate("100 ÷ 4").getOrThrow().formattedResult)
    }

    @Test
    fun testOperatorPrecedenceAndParentheses() {
        assertEquals("14", engine.evaluate("2 + 3 × 4").getOrThrow().formattedResult)
        assertEquals("20", engine.evaluate("(2 + 3) × 4").getOrThrow().formattedResult)
        assertEquals("20", engine.evaluate("(5 + 5) × 2").getOrThrow().formattedResult)
    }

    @Test
    fun testExponentiationRightAssociativity() {
        // 2^3 = 8
        assertEquals("8", engine.evaluate("2 ^ 3").getOrThrow().formattedResult)
        // 2^3^2 should evaluate to 2^(3^2) = 2^9 = 512
        assertEquals("512", engine.evaluate("2 ^ 3 ^ 2").getOrThrow().formattedResult)
    }

    @Test
    fun testRootsAndFactorials() {
        assertEquals("12", engine.evaluate("√144").getOrThrow().formattedResult)
        assertEquals("120", engine.evaluate("5!").getOrThrow().formattedResult)
        assertEquals("1", engine.evaluate("0!").getOrThrow().formattedResult)
    }

    @Test
    fun testPercentage() {
        assertEquals("5", engine.evaluate("100 × 5%").getOrThrow().formattedResult)
        assertEquals("0.5", engine.evaluate("50%").getOrThrow().formattedResult)
    }

    @Test
    fun testTrigonometryInDegrees() {
        engine.angleMode = AngleMode.DEG
        assertEquals(0.5, engine.evaluate("sin(30)").getOrThrow().result, 1e-6)
        assertEquals(0.5, engine.evaluate("cos(60)").getOrThrow().result, 1e-6)
        assertEquals(1.0, engine.evaluate("tan(45)").getOrThrow().result, 1e-6)
        assertEquals(0.0, engine.evaluate("sin(180)").getOrThrow().result, 1e-6)
        assertEquals(0.0, engine.evaluate("cos(90)").getOrThrow().result, 1e-6)
    }

    @Test
    fun testTrigonometryInRadians() {
        engine.angleMode = AngleMode.RAD
        assertEquals(0.0, engine.evaluate("sin(π)").getOrThrow().result, 1e-6)
        assertEquals(1.0, engine.evaluate("cos(0)").getOrThrow().result, 1e-6)
    }

    @Test
    fun testLogarithms() {
        assertEquals(2.0, engine.evaluate("log(100)").getOrThrow().result, 1e-6)
        assertEquals(1.0, engine.evaluate("ln(e)").getOrThrow().result, 1e-6)
        assertEquals(3.0, engine.evaluate("log2(8)").getOrThrow().result, 1e-6)
    }

    @Test
    fun testImplicitMultiplication() {
        assertEquals(6.0, engine.evaluate("2(3)").getOrThrow().result, 1e-6)
        assertEquals(20.0, engine.evaluate("(2+3)(4)").getOrThrow().result, 1e-6)
        val twoPi = engine.evaluate("2π").getOrThrow().result
        assertEquals(2 * Math.PI, twoPi, 1e-6)
    }

    @Test
    fun testDivisionByZeroError() {
        val result = engine.evaluate("10 ÷ 0")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CalculationError.DivisionByZero)
    }

    @Test
    fun testDomainErrorForNegativeRoot() {
        val result = engine.evaluate("√(-9)")
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is CalculationError.DomainError)
    }

    @Test
    fun testMemoryOperations() {
        engine.memory.clear()
        assertFalse(engine.memory.hasMemory())
        engine.memory.store(42.0)
        assertTrue(engine.memory.hasMemory())
        assertEquals(42.0, engine.memory.recall(), 1e-6)
        engine.memory.add(8.0)
        assertEquals(50.0, engine.memory.recall(), 1e-6)
        engine.memory.subtract(20.0)
        assertEquals(30.0, engine.memory.recall(), 1e-6)
        engine.memory.clear()
        assertFalse(engine.memory.hasMemory())
    }
}
