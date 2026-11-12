package com.calcx.calculator.domain.finance

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object FinanceEngine {

    private val MC = MathContext(16, RoundingMode.HALF_UP)

    // --- 8.1 GST CALCULATIONS ---
    data class GstResult(
        val originalAmount: BigDecimal,
        val gstRate: BigDecimal,
        val isInclusive: Boolean,
        val isInterState: Boolean,
        val taxableAmount: BigDecimal,
        val totalGst: BigDecimal,
        val cgst: BigDecimal,
        val sgst: BigDecimal,
        val igst: BigDecimal,
        val finalAmount: BigDecimal
    )

    fun calculateGst(
        amount: Double,
        ratePercent: Double,
        isInclusive: Boolean,
        isInterState: Boolean = false
    ): GstResult {
        val amt = BigDecimal(amount.toString())
        val rate = BigDecimal(ratePercent.toString())
        val hundred = BigDecimal("100")
        val two = BigDecimal("2")

        val taxableAmount: BigDecimal
        val totalGst: BigDecimal
        val finalAmount: BigDecimal

        if (isInclusive) {
            // Amount = Taxable + (Taxable * Rate / 100) = Taxable * (1 + Rate / 100)
            // Taxable = Amount / (1 + Rate / 100) = (Amount * 100) / (100 + Rate)
            val divisor = hundred.add(rate)
            taxableAmount = amt.multiply(hundred).divide(divisor, 2, RoundingMode.HALF_UP)
            totalGst = amt.subtract(taxableAmount).setScale(2, RoundingMode.HALF_UP)
            finalAmount = amt.setScale(2, RoundingMode.HALF_UP)
        } else {
            taxableAmount = amt.setScale(2, RoundingMode.HALF_UP)
            totalGst = taxableAmount.multiply(rate).divide(hundred, 2, RoundingMode.HALF_UP)
            finalAmount = taxableAmount.add(totalGst).setScale(2, RoundingMode.HALF_UP)
        }

        val cgst = if (!isInterState) totalGst.divide(two, 2, RoundingMode.HALF_UP) else BigDecimal.ZERO.setScale(2)
        val sgst = if (!isInterState) totalGst.subtract(cgst) else BigDecimal.ZERO.setScale(2)
        val igst = if (isInterState) totalGst else BigDecimal.ZERO.setScale(2)

        return GstResult(
            originalAmount = amt,
            gstRate = rate,
            isInclusive = isInclusive,
            isInterState = isInterState,
            taxableAmount = taxableAmount,
            totalGst = totalGst,
            cgst = cgst,
            sgst = sgst,
            igst = igst,
            finalAmount = finalAmount
        )
    }

    // --- 8.2 EMI CALCULATIONS ---
    data class AmortizationRow(
        val month: Int,
        val openingBalance: BigDecimal,
        val emi: BigDecimal,
        val principalPaid: BigDecimal,
        val interestPaid: BigDecimal,
        val closingBalance: BigDecimal
    )

    data class EmiResult(
        val principal: BigDecimal,
        val annualRatePercent: BigDecimal,
        val tenureMonths: Int,
        val monthlyEmi: BigDecimal,
        val totalInterest: BigDecimal,
        val totalPayment: BigDecimal,
        val principalPercent: Float,
        val interestPercent: Float,
        val amortizationSchedule: List<AmortizationRow>
    )

    fun calculateEmi(
        principalAmount: Double,
        annualRatePercent: Double,
        tenureMonths: Int
    ): EmiResult {
        val p = BigDecimal(principalAmount.toString())
        val rate = BigDecimal(annualRatePercent.toString())

        if (tenureMonths <= 0) {
            throw IllegalArgumentException("Tenure must be greater than 0")
        }

        val monthlyEmi: BigDecimal
        val totalPayment: BigDecimal
        val totalInterest: BigDecimal

        if (annualRatePercent == 0.0) {
            monthlyEmi = p.divide(BigDecimal(tenureMonths), 2, RoundingMode.HALF_UP)
            totalPayment = p.setScale(2, RoundingMode.HALF_UP)
            totalInterest = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        } else {
            // Monthly rate r = annualRate / 12 / 100
            val monthlyRateDouble = annualRatePercent / 12.0 / 100.0
            val r = BigDecimal(monthlyRateDouble.toString())
            val onePlusR = BigDecimal.ONE.add(r)
            val onePlusRPowN = onePlusR.pow(tenureMonths, MC)

            // EMI = P * r * (1+r)^n / ((1+r)^n - 1)
            val numerator = p.multiply(r, MC).multiply(onePlusRPowN, MC)
            val denominator = onePlusRPowN.subtract(BigDecimal.ONE, MC)

            monthlyEmi = numerator.divide(denominator, 2, RoundingMode.HALF_UP)
            totalPayment = monthlyEmi.multiply(BigDecimal(tenureMonths)).setScale(2, RoundingMode.HALF_UP)
            totalInterest = totalPayment.subtract(p).setScale(2, RoundingMode.HALF_UP)
        }

        // Amortization schedule
        val schedule = mutableListOf<AmortizationRow>()
        var balance = p
        val monthlyRate = annualRatePercent / 12.0 / 100.0

        for (m in 1..tenureMonths) {
            val opening = balance
            val interestPart = (opening.toDouble() * monthlyRate).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
            val principalPart = if (m == tenureMonths) {
                opening
            } else {
                monthlyEmi.subtract(interestPart).setScale(2, RoundingMode.HALF_UP)
            }
            balance = opening.subtract(principalPart).coerceAtLeast(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP)

            schedule.add(
                AmortizationRow(
                    month = m,
                    openingBalance = opening,
                    emi = monthlyEmi,
                    principalPaid = principalPart,
                    interestPaid = interestPart,
                    closingBalance = balance
                )
            )
        }

        val totalD = totalPayment.toDouble()
        val principalPct = if (totalD > 0) ((p.toDouble() / totalD) * 100).toFloat() else 100f
        val interestPct = if (totalD > 0) ((totalInterest.toDouble() / totalD) * 100).toFloat() else 0f

        return EmiResult(
            principal = p.setScale(2, RoundingMode.HALF_UP),
            annualRatePercent = rate,
            tenureMonths = tenureMonths,
            monthlyEmi = monthlyEmi,
            totalInterest = totalInterest,
            totalPayment = totalPayment,
            principalPercent = principalPct,
            interestPercent = interestPct,
            amortizationSchedule = schedule
        )
    }

    // --- 8.3 & 8.4 INTEREST (SIMPLE & COMPOUND) ---
    data class InterestResult(
        val principal: BigDecimal,
        val ratePercent: BigDecimal,
        val timeYears: Double,
        val interestAmount: BigDecimal,
        val totalAmount: BigDecimal
    )

    fun calculateSimpleInterest(principal: Double, ratePercent: Double, timeYears: Double): InterestResult {
        val p = BigDecimal(principal.toString())
        val r = BigDecimal(ratePercent.toString())
        val t = BigDecimal(timeYears.toString())

        val interest = p.multiply(r).multiply(t).divide(BigDecimal("100"), 2, RoundingMode.HALF_UP)
        val total = p.add(interest).setScale(2, RoundingMode.HALF_UP)

        return InterestResult(p, r, timeYears, interest, total)
    }

    fun calculateCompoundInterest(
        principal: Double,
        ratePercent: Double,
        timeYears: Double,
        compoundingFrequencyPerYear: Int = 1
    ): InterestResult {
        val p = BigDecimal(principal.toString())
        val r = ratePercent / 100.0
        val n = compoundingFrequencyPerYear.toDouble()
        val t = timeYears

        // A = P * (1 + r/n)^(n*t)
        val factor = Math.pow(1.0 + (r / n), n * t)
        val total = p.multiply(BigDecimal(factor.toString())).setScale(2, RoundingMode.HALF_UP)
        val interest = total.subtract(p).setScale(2, RoundingMode.HALF_UP)

        return InterestResult(p, BigDecimal(ratePercent.toString()), timeYears, interest, total)
    }

    // --- 8.5 SIP CALCULATOR ---
    data class SipResult(
        val monthlyInvestment: BigDecimal,
        val annualExpectedReturn: BigDecimal,
        val investmentYears: Double,
        val totalInvested: BigDecimal,
        val estimatedReturns: BigDecimal,
        val maturityAmount: BigDecimal
    )

    fun calculateSip(
        monthlyAmount: Double,
        annualExpectedReturnPercent: Double,
        investmentYears: Double
    ): SipResult {
        val p = BigDecimal(monthlyAmount.toString())
        val totalMonths = (investmentYears * 12).toInt()
        val i = annualExpectedReturnPercent / 12.0 / 100.0

        val totalInvested = p.multiply(BigDecimal(totalMonths)).setScale(2, RoundingMode.HALF_UP)

        val maturity = if (i == 0.0) {
            totalInvested
        } else {
            // M = P * [ (1 + i)^n - 1 ] * (1 + i) / i
            val compoundFactor = Math.pow(1.0 + i, totalMonths.toDouble()) - 1.0
            val maturityDouble = monthlyAmount * compoundFactor * (1.0 + i) / i
            BigDecimal(maturityDouble.toString()).setScale(2, RoundingMode.HALF_UP)
        }

        val estimatedReturns = maturity.subtract(totalInvested).setScale(2, RoundingMode.HALF_UP)

        return SipResult(
            monthlyInvestment = p.setScale(2, RoundingMode.HALF_UP),
            annualExpectedReturn = BigDecimal(annualExpectedReturnPercent.toString()),
            investmentYears = investmentYears,
            totalInvested = totalInvested,
            estimatedReturns = estimatedReturns,
            maturityAmount = maturity
        )
    }

    // --- 8.6 DISCOUNT CALCULATOR ---
    data class DiscountResult(
        val originalPrice: BigDecimal,
        val discountPercent: BigDecimal,
        val additionalDiscountPercent: BigDecimal,
        val amountSaved: BigDecimal,
        val finalPrice: BigDecimal
    )

    fun calculateDiscount(
        price: Double,
        discountPercent: Double,
        additionalDiscountPercent: Double = 0.0
    ): DiscountResult {
        val orig = BigDecimal(price.toString())
        val d1 = BigDecimal(discountPercent.toString())
        val d2 = BigDecimal(additionalDiscountPercent.toString())
        val hundred = BigDecimal("100")

        val save1 = orig.multiply(d1).divide(hundred, 2, RoundingMode.HALF_UP)
        val intermediate = orig.subtract(save1)

        val save2 = if (additionalDiscountPercent > 0.0) {
            intermediate.multiply(d2).divide(hundred, 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO.setScale(2)
        }

        val totalSaved = save1.add(save2).setScale(2, RoundingMode.HALF_UP)
        val finalPrice = orig.subtract(totalSaved).setScale(2, RoundingMode.HALF_UP)

        return DiscountResult(
            originalPrice = orig.setScale(2, RoundingMode.HALF_UP),
            discountPercent = d1,
            additionalDiscountPercent = d2,
            amountSaved = totalSaved,
            finalPrice = finalPrice
        )
    }

    // --- 8.7 PROFIT & LOSS CALCULATOR ---
    data class ProfitLossResult(
        val costPrice: BigDecimal,
        val sellingPrice: BigDecimal,
        val isProfit: Boolean,
        val differenceAmount: BigDecimal,
        val percentage: BigDecimal,
        val marginPercent: BigDecimal,
        val markupPercent: BigDecimal
    )

    fun calculateProfitLoss(costPrice: Double, sellingPrice: Double): ProfitLossResult {
        val cp = BigDecimal(costPrice.toString())
        val sp = BigDecimal(sellingPrice.toString())
        val diff = sp.subtract(cp)
        val isProfit = diff >= BigDecimal.ZERO
        val absDiff = diff.abs()

        val hundred = BigDecimal("100")

        val percentage = if (cp > BigDecimal.ZERO) {
            absDiff.multiply(hundred).divide(cp, 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO.setScale(2)

        val margin = if (sp > BigDecimal.ZERO) {
            diff.multiply(hundred).divide(sp, 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO.setScale(2)

        val markup = if (cp > BigDecimal.ZERO) {
            diff.multiply(hundred).divide(cp, 2, RoundingMode.HALF_UP)
        } else BigDecimal.ZERO.setScale(2)

        return ProfitLossResult(
            costPrice = cp.setScale(2, RoundingMode.HALF_UP),
            sellingPrice = sp.setScale(2, RoundingMode.HALF_UP),
            isProfit = isProfit,
            differenceAmount = absDiff.setScale(2, RoundingMode.HALF_UP),
            percentage = percentage,
            marginPercent = margin,
            markupPercent = markup
        )
    }

    // --- 8.8 BREAK-EVEN CALCULATOR ---
    data class BreakEvenResult(
        val fixedCosts: BigDecimal,
        val variableCostPerUnit: BigDecimal,
        val sellingPricePerUnit: BigDecimal,
        val contributionMarginPerUnit: BigDecimal,
        val contributionMarginRatio: BigDecimal,
        val breakEvenQuantity: BigDecimal,
        val breakEvenRevenue: BigDecimal
    )

    fun calculateBreakEven(
        fixedCosts: Double,
        variableCostPerUnit: Double,
        sellingPricePerUnit: Double
    ): BreakEvenResult {
        val fc = BigDecimal(fixedCosts.toString())
        val vc = BigDecimal(variableCostPerUnit.toString())
        val sp = BigDecimal(sellingPricePerUnit.toString())

        val cm = sp.subtract(vc)
        if (cm <= BigDecimal.ZERO) {
            throw IllegalArgumentException("Selling price must exceed variable cost to break even.")
        }

        val cmRatio = cm.divide(sp, 4, RoundingMode.HALF_UP).multiply(BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
        val breakEvenUnits = fc.divide(cm, 0, RoundingMode.CEILING)
        val breakEvenRevenue = breakEvenUnits.multiply(sp).setScale(2, RoundingMode.HALF_UP)

        return BreakEvenResult(
            fixedCosts = fc.setScale(2, RoundingMode.HALF_UP),
            variableCostPerUnit = vc.setScale(2, RoundingMode.HALF_UP),
            sellingPricePerUnit = sp.setScale(2, RoundingMode.HALF_UP),
            contributionMarginPerUnit = cm.setScale(2, RoundingMode.HALF_UP),
            contributionMarginRatio = cmRatio,
            breakEvenQuantity = breakEvenUnits,
            breakEvenRevenue = breakEvenRevenue
        )
    }
}
