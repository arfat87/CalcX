package com.calcx.calculator.engine

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

object NumberFormatter {

    fun format(
        value: Double,
        maxDecimals: Int = 8,
        useThousandsSeparator: Boolean = true,
        forceScientific: Boolean = false
    ): String {
        if (value.isNaN()) return "Error"
        if (value.isInfinite()) return if (value > 0) "∞" else "-∞"

        val absVal = abs(value)

        // Use scientific notation if forced or if value is extremely large/small
        if (forceScientific || (absVal != 0.0 && (absVal >= 1e12 || absVal < 1e-9))) {
            return String.format(Locale.US, "%.${minOf(maxDecimals, 6)}e", value)
                .replace("e+0", "e")
                .replace("e+", "e")
                .replace("e-0", "e-")
        }

        val symbols = DecimalFormatSymbols(Locale.US).apply {
            groupingSeparator = if (useThousandsSeparator) ',' else '\u0000'
            decimalSeparator = '.'
        }

        val pattern = buildString {
            if (useThousandsSeparator) {
                append("#,##0")
            } else {
                append("0")
            }
            if (maxDecimals > 0) {
                append(".")
                repeat(maxDecimals) { append("#") }
            }
        }

        val df = DecimalFormat(pattern, symbols).apply {
            isGroupingUsed = useThousandsSeparator
        }

        return df.format(value)
    }

    fun cleanInput(expression: String): String {
        return expression
            .replace("×", "*")
            .replace("÷", "/")
            .replace("−", "-")
    }
}
