package com.calcx.calculator.feature.advancedmath

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.domain.advancedmath.AdvancedMathEngine

// --- 1. STATISTICS SCREEN ---
@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    var inputText by remember { mutableStateOf("12, 15, 12, 18, 22, 25, 30, 18, 24") }

    val numbers = remember(inputText) {
        inputText.split(",", " ", "\n")
            .mapNotNull { it.trim().toDoubleOrNull() }
    }

    val stats = remember(numbers) {
        if (numbers.isNotEmpty()) {
            try { AdvancedMathEngine.calculateStatistics(numbers) } catch (e: Exception) { null }
        } else null
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Statistics", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = { Text("Numbers (comma or space separated)") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (stats != null) {
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Summary Statistics (N = ${stats.count})", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    StatRow("Mean (Average):", String.format("%.4f", stats.mean))
                    StatRow("Median:", String.format("%.4f", stats.median))
                    StatRow("Mode:", if (stats.modes.isNotEmpty()) stats.modes.joinToString(", ") else "None")
                    StatRow("Min / Max:", "${stats.min} / ${stats.max}")
                    StatRow("Range:", "${stats.range}")
                    StatRow("Sum:", "${stats.sum}")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = colors.border)

                    Text("Dispersion & Variances", color = colors.accent, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    StatRow("Sample Std Dev (s):", String.format("%.4f", stats.sampleStdDev))
                    StatRow("Sample Variance (s²):", String.format("%.4f", stats.sampleVariance))
                    StatRow("Population Std Dev (σ):", String.format("%.4f", stats.populationStdDev))
                    StatRow("Population Variance (σ²):", String.format("%.4f", stats.populationVariance))
                    StatRow("Quartiles (Q1 / Q3):", "${String.format("%.2f", stats.q1)} / ${String.format("%.2f", stats.q3)}")
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    val colors = CalcXTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = colors.textSecondary, fontSize = 14.sp)
        Text(value, color = colors.textPrimary, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

// --- 2. FRACTIONS SCREEN ---
@Composable
fun FractionsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var num1Text by remember { mutableStateOf("1") }
    var den1Text by remember { mutableStateOf("2") }
    var num2Text by remember { mutableStateOf("1") }
    var den2Text by remember { mutableStateOf("3") }
    var op by remember { mutableStateOf("+") }

    val f1 = remember(num1Text, den1Text) {
        val n = num1Text.toLongOrNull() ?: 0L
        val d = den1Text.toLongOrNull() ?: 1L
        if (d != 0L) AdvancedMathEngine.Fraction(n, d) else null
    }

    val f2 = remember(num2Text, den2Text) {
        val n = num2Text.toLongOrNull() ?: 0L
        val d = den2Text.toLongOrNull() ?: 1L
        if (d != 0L) AdvancedMathEngine.Fraction(n, d) else null
    }

    val result = remember(f1, f2, op) {
        if (f1 != null && f2 != null) {
            try {
                when (op) {
                    "+" -> f1 + f2
                    "-" -> f1 - f2
                    "×" -> f1 * f2
                    "÷" -> f1 / f2
                    else -> null
                }
            } catch (e: Exception) {
                null
            }
        } else null
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Fractions", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fraction 1 & Fraction 2 inputs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Fraction 1
                Column(modifier = Modifier.width(100.dp)) {
                    OutlinedTextField(
                        value = num1Text,
                        onValueChange = { num1Text = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 2.dp, color = colors.textPrimary)
                    OutlinedTextField(
                        value = den1Text,
                        onValueChange = { den1Text = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Operator selector
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    for (operator in listOf("+", "-", "×", "÷")) {
                        FilterChip(
                            selected = op == operator,
                            onClick = { op = operator },
                            label = { Text(operator, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // Fraction 2
                Column(modifier = Modifier.width(100.dp)) {
                    OutlinedTextField(
                        value = num2Text,
                        onValueChange = { num2Text = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 2.dp, color = colors.textPrimary)
                    OutlinedTextField(
                        value = den2Text,
                        onValueChange = { den2Text = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (result != null) {
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Result", color = colors.textSecondary, fontSize = 13.sp)
                    Text(result.toString(), color = colors.accent, fontSize = 34.sp, fontWeight = FontWeight.Bold)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                    StatRow("Mixed Fraction:", result.toMixedString())
                    val decimalVal = result.numerator.toDouble() / result.denominator.toDouble()
                    StatRow("Decimal Value:", String.format("%.6f", decimalVal))
                }
            }
        }
    }
}

// --- 3. MATRIX CALCULATOR SCREEN ---
@Composable
fun MatrixCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var a00 by remember { mutableStateOf("1") }
    var a01 by remember { mutableStateOf("2") }
    var a10 by remember { mutableStateOf("3") }
    var a11 by remember { mutableStateOf("4") }

    val matrixA = remember(a00, a01, a10, a11) {
        val d00 = a00.toDoubleOrNull() ?: 0.0
        val d01 = a01.toDoubleOrNull() ?: 0.0
        val d10 = a10.toDoubleOrNull() ?: 0.0
        val d11 = a11.toDoubleOrNull() ?: 0.0
        AdvancedMathEngine.Matrix(2, 2, listOf(listOf(d00, d01), listOf(d10, d11)))
    }

    val det = remember(matrixA) { matrixA.determinant() }
    val inv = remember(matrixA) {
        try { matrixA.inverse() } catch (e: Exception) { null }
    }
    val trans = remember(matrixA) { matrixA.transpose() }

    Scaffold(
        topBar = { CalcXTopBar(title = "Matrix Calculator (2x2)", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Matrix A Input", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)

            // 2x2 grid
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = a00,
                    onValueChange = { a00 = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = a01,
                    onValueChange = { a01 = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = a10,
                    onValueChange = { a10 = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = a11,
                    onValueChange = { a11 = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Properties of Matrix A", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(10.dp))

                StatRow("Determinant det(A):", String.format("%.4f", det))

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = colors.border)

                Text("Transpose Aᵀ:", color = colors.textSecondary, fontSize = 13.sp)
                Text("[ ${trans.data[0][0]}, ${trans.data[0][1]} ]\n[ ${trans.data[1][0]}, ${trans.data[1][1]} ]", color = colors.textPrimary, fontWeight = FontWeight.Medium)

                Spacer(modifier = Modifier.height(10.dp))

                Text("Inverse A⁻¹:", color = colors.textSecondary, fontSize = 13.sp)
                if (inv != null) {
                    Text(
                        "[ ${String.format("%.3f", inv.data[0][0])}, ${String.format("%.3f", inv.data[0][1])} ]\n" +
                        "[ ${String.format("%.3f", inv.data[1][0])}, ${String.format("%.3f", inv.data[1][1])} ]",
                        color = colors.accent,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text("Singular matrix (det = 0), inverse does not exist", color = Color(0xFFFF453A))
                }
            }
        }
    }
}

// --- 4. EQUATION SOLVER SCREEN ---
@Composable
fun EquationSolverScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var aText by remember { mutableStateOf("1") }
    var bText by remember { mutableStateOf("-5") }
    var cText by remember { mutableStateOf("6") }

    val a = aText.toDoubleOrNull() ?: 1.0
    val b = bText.toDoubleOrNull() ?: 0.0
    val c = cText.toDoubleOrNull() ?: 0.0

    val quadSolution = remember(a, b, c) {
        AdvancedMathEngine.solveQuadratic(a, b, c)
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Equation Solver", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Quadratic Equation: ax² + bx + c = 0", color = colors.textPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = aText,
                    onValueChange = { aText = it },
                    label = { Text("a") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = bText,
                    onValueChange = { bText = it },
                    label = { Text("b") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = cText,
                    onValueChange = { cText = it },
                    label = { Text("c") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Solutions", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(10.dp))

                when (quadSolution) {
                    is AdvancedMathEngine.QuadraticSolution.TwoReal -> {
                        StatRow("Root 1 (x₁):", String.format("%.4f", quadSolution.x1))
                        StatRow("Root 2 (x₂):", String.format("%.4f", quadSolution.x2))
                        Text("Discriminant Δ > 0 (Two distinct real roots)", color = Color(0xFF30D158), fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                    is AdvancedMathEngine.QuadraticSolution.OneReal -> {
                        StatRow("Single Root (x):", String.format("%.4f", quadSolution.x))
                        Text("Discriminant Δ = 0 (One repeated real root)", color = colors.accent, fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                    is AdvancedMathEngine.QuadraticSolution.Complex -> {
                        StatRow("Root 1 (x₁):", "${String.format("%.4f", quadSolution.real)} + ${String.format("%.4f", quadSolution.imag)}i")
                        StatRow("Root 2 (x₂):", "${String.format("%.4f", quadSolution.real)} - ${String.format("%.4f", quadSolution.imag)}i")
                        Text("Discriminant Δ < 0 (Complex conjugate roots)", color = Color(0xFFBF5AF2), fontSize = 12.sp, modifier = Modifier.padding(top = 6.dp))
                    }
                    is AdvancedMathEngine.QuadraticSolution.LinearFallback -> {
                        Text("a = 0: Degenerates to linear equation", color = colors.textSecondary)
                    }
                }
            }
        }
    }
}
