package com.calcx.calculator.feature.finance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.domain.finance.FinanceEngine

@Composable
fun EmiCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var principalText by remember { mutableStateOf("500000") }
    var rateText by remember { mutableStateOf("8.5") }
    var tenureText by remember { mutableStateOf("5") }
    var isTenureInYears by remember { mutableStateOf(true) }
    var showAmortization by remember { mutableStateOf(false) }

    val principal = principalText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val rawTenure = tenureText.toIntOrNull() ?: 1
    val tenureMonths = if (isTenureInYears) rawTenure * 12 else rawTenure

    val emiResult = remember(principal, rate, tenureMonths) {
        try {
            FinanceEngine.calculateEmi(principal, rate, maxOf(1, tenureMonths))
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "EMI Calculator",
                onBackClick = onBackClick
            )
        },
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
            // Principal Input
            OutlinedTextField(
                value = principalText,
                onValueChange = { principalText = it },
                label = { Text("Loan Amount / Principal (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surfacePrimary,
                    unfocusedContainerColor = colors.surfacePrimary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Interest Rate Input
            OutlinedTextField(
                value = rateText,
                onValueChange = { rateText = it },
                label = { Text("Annual Interest Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surfacePrimary,
                    unfocusedContainerColor = colors.surfacePrimary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Tenure Input with Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = tenureText,
                    onValueChange = { tenureText = it },
                    label = { Text("Tenure") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accent,
                        unfocusedBorderColor = colors.border,
                        focusedContainerColor = colors.surfacePrimary,
                        unfocusedContainerColor = colors.surfacePrimary,
                        focusedTextColor = colors.textPrimary,
                        unfocusedTextColor = colors.textPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = isTenureInYears,
                        onClick = { isTenureInYears = true },
                        label = { Text("Yr") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.accent,
                            selectedLabelColor = Color.White,
                            containerColor = colors.surfacePrimary,
                            labelColor = colors.textPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    FilterChip(
                        selected = !isTenureInYears,
                        onClick = { isTenureInYears = false },
                        label = { Text("Mo") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = colors.accent,
                            selectedLabelColor = Color.White,
                            containerColor = colors.surfacePrimary,
                            labelColor = colors.textPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            if (emiResult != null) {
                // Main Monthly EMI Card
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Monthly EMI",
                        color = colors.textSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "₹${emiResult.monthlyEmi}",
                        color = colors.accent,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Interest", color = colors.textSecondary, fontSize = 12.sp)
                            Text("₹${emiResult.totalInterest}", color = Color(0xFFFF453A), fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Payment", color = colors.textSecondary, fontSize = 12.sp)
                            Text("₹${emiResult.totalPayment}", color = colors.textPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar: Principal vs Interest
                    Text(
                        text = "Breakdown: Principal (${String.format("%.1f", emiResult.principalPercent)}%) vs Interest (${String.format("%.1f", emiResult.interestPercent)}%)",
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color(0xFFFF453A))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(emiResult.principalPercent / 100f)
                                .background(colors.accent)
                        )
                    }
                }

                // Amortization Schedule Accordion
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Amortization Schedule (${emiResult.amortizationSchedule.size} Months)",
                            color = colors.textPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = { showAmortization = !showAmortization }) {
                            Icon(
                                imageVector = if (showAmortization) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle Schedule",
                                tint = colors.textPrimary
                            )
                        }
                    }

                    AnimatedVisibility(visible = showAmortization) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(colors.surfaceSecondary)
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Mo", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f))
                                Text("Principal", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f), textAlign = TextAlign.End)
                                Text("Interest", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f), textAlign = TextAlign.End)
                                Text("Balance", color = colors.textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.4f), textAlign = TextAlign.End)
                            }

                            // First 24 or all rows
                            emiResult.amortizationSchedule.take(36).forEach { row ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${row.month}", color = colors.textPrimary, fontSize = 12.sp, modifier = Modifier.weight(0.7f))
                                    Text("₹${row.principalPaid}", color = colors.accent, fontSize = 12.sp, modifier = Modifier.weight(1.3f), textAlign = TextAlign.End)
                                    Text("₹${row.interestPaid}", color = Color(0xFFFF453A), fontSize = 12.sp, modifier = Modifier.weight(1.3f), textAlign = TextAlign.End)
                                    Text("₹${row.closingBalance}", color = colors.textSecondary, fontSize = 12.sp, modifier = Modifier.weight(1.4f), textAlign = TextAlign.End)
                                }
                                HorizontalDivider(color = colors.border.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}
