package com.calcx.calculator.domain.finance

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class FinanceEngineTest {

    @Test
    fun testGstExclusive() {
        val result = FinanceEngine.calculateGst(1000.0, 5.0, isInclusive = false, isInterState = false)
        assertEquals(BigDecimal("1000.00"), result.taxableAmount)
        assertEquals(BigDecimal("50.00"), result.totalGst)
        assertEquals(BigDecimal("25.00"), result.cgst)
        assertEquals(BigDecimal("25.00"), result.sgst)
        assertEquals(BigDecimal("0.00"), result.igst)
        assertEquals(BigDecimal("1050.00"), result.finalAmount)
    }

    @Test
    fun testGstInclusive() {
        val result = FinanceEngine.calculateGst(1050.0, 5.0, isInclusive = true, isInterState = false)
        assertEquals(BigDecimal("1000.00"), result.taxableAmount)
        assertEquals(BigDecimal("50.00"), result.totalGst)
        assertEquals(BigDecimal("25.00"), result.cgst)
        assertEquals(BigDecimal("25.00"), result.sgst)
        assertEquals(BigDecimal("1050.00"), result.finalAmount)
    }

    @Test
    fun testGstInterState() {
        val result = FinanceEngine.calculateGst(1000.0, 18.0, isInclusive = false, isInterState = true)
        assertEquals(BigDecimal("180.00"), result.igst)
        assertEquals(BigDecimal("0.00"), result.cgst)
        assertEquals(BigDecimal("0.00"), result.sgst)
        assertEquals(BigDecimal("1180.00"), result.finalAmount)
    }

    @Test
    fun testEmiStandard() {
        // Principal 100,000 at 12% per annum for 12 months
        // Monthly rate r = 0.01
        // EMI formula = 100000 * 0.01 * (1.01^12) / (1.01^12 - 1) = 8884.88
        val result = FinanceEngine.calculateEmi(100000.0, 12.0, 12)
        assertEquals(BigDecimal("8884.88"), result.monthlyEmi)
        assertEquals(12, result.amortizationSchedule.size)
        assertTrue(result.totalInterest > BigDecimal.ZERO)
    }

    @Test
    fun testEmiZeroInterest() {
        val result = FinanceEngine.calculateEmi(12000.0, 0.0, 12)
        assertEquals(BigDecimal("1000.00"), result.monthlyEmi)
        assertEquals(BigDecimal("0.00"), result.totalInterest)
        assertEquals(BigDecimal("12000.00"), result.totalPayment)
    }

    @Test
    fun testSimpleInterest() {
        val result = FinanceEngine.calculateSimpleInterest(10000.0, 5.0, 2.0)
        assertEquals(BigDecimal("1000.00"), result.interestAmount)
        assertEquals(BigDecimal("11000.00"), result.totalAmount)
    }

    @Test
    fun testDiscount() {
        val result = FinanceEngine.calculateDiscount(1000.0, 20.0, 10.0)
        // 1000 - 20% = 800; 800 - 10% = 720; Total saved = 280
        assertEquals(BigDecimal("280.00"), result.amountSaved)
        assertEquals(BigDecimal("720.00"), result.finalPrice)
    }

    @Test
    fun testProfitLoss() {
        val result = FinanceEngine.calculateProfitLoss(100.0, 120.0)
        assertTrue(result.isProfit)
        assertEquals(BigDecimal("20.00"), result.differenceAmount)
        assertEquals(BigDecimal("20.00"), result.markupPercent)
        assertEquals(BigDecimal("16.67"), result.marginPercent)
    }
}
