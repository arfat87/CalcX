package com.calcx.calculator.feature.finance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.domain.finance.FinanceEngine

@Composable
fun GstCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var amountText by remember { mutableStateOf("1000") }
    var selectedPresetRate by remember { mutableDoubleStateOf(18.0) }
    var customRateText by remember { mutableStateOf("") }
    var isInclusive by remember { mutableStateOf(false) }
    var isInterState by remember { mutableStateOf(false) }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val activeRate = customRateText.toDoubleOrNull() ?: selectedPresetRate

    val result = remember(amount, activeRate, isInclusive, isInterState) {
        FinanceEngine.calculateGst(amount, activeRate, isInclusive, isInterState)
    }

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "GST Calculator",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = {
                        val report = "GST Breakdown:\nTaxable: ₹${result.taxableAmount}\nTotal GST: ₹${result.totalGst} (${result.gstRate}%)\nCGST: ₹${result.cgst}\nSGST: ₹${result.sgst}\nIGST: ₹${result.igst}\nFinal Total: ₹${result.finalAmount}"
                        clipboard.setText(AnnotatedString(report))
                        Toast.makeText(context, "Copied GST Breakdown", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = colors.textPrimary)
                    }
                }
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
            // Amount Input
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Initial Amount (₹)") },
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

            // Preset Rates Chips
            Text(text = "GST Rate Presets", color = colors.textSecondary, fontSize = 14.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val presets = listOf(5.0, 12.0, 18.0, 28.0)
                presets.forEach { rate ->
                    FilterChip(
                        selected = customRateText.isEmpty() && selectedPresetRate == rate,
                        onClick = {
                            customRateText = ""
                            selectedPresetRate = rate
                        },
                        label = { Text("${rate.toInt()}%") },
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

            // Custom Rate Input
            OutlinedTextField(
                value = customRateText,
                onValueChange = { customRateText = it },
                label = { Text("Custom GST % (Optional)") },
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

            // Inclusive / Exclusive & Inter-State Switches
            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("GST Inclusive Mode", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Calculate backward from gross price", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = isInclusive,
                        onCheckedChange = { isInclusive = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colors.accent)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Inter-State Supply (IGST)", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Apply IGST instead of CGST + SGST", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = isInterState,
                        onCheckedChange = { isInterState = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colors.accent)
                    )
                }
            }

            // Calculation Results Breakdown
            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Tax Calculation Summary",
                    color = colors.accent,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                GstResultRow("Taxable Amount:", "₹${result.taxableAmount}", colors.textPrimary)
                GstResultRow("GST Rate:", "${result.gstRate}%", colors.textSecondary)

                if (isInterState) {
                    GstResultRow("IGST:", "₹${result.igst}", colors.textPrimary)
                } else {
                    GstResultRow("CGST (${result.gstRate.divide(java.math.BigDecimal(2))} %):", "₹${result.cgst}", colors.textPrimary)
                    GstResultRow("SGST (${result.gstRate.divide(java.math.BigDecimal(2))} %):", "₹${result.sgst}", colors.textPrimary)
                }

                GstResultRow("Total GST Tax:", "₹${result.totalGst}", colors.accent, isBold = true)

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = colors.border)

                GstResultRow("Final Total Amount:", "₹${result.finalAmount}", colors.accent, isLarge = true)
            }
        }
    }
}

@Composable
private fun GstResultRow(
    label: String,
    value: String,
    valueColor: Color,
    isBold: Boolean = false,
    isLarge: Boolean = false
) {
    val colors = CalcXTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = colors.textSecondary,
            fontSize = if (isLarge) 16.sp else 14.sp,
            fontWeight = if (isLarge || isBold) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = if (isLarge) 20.sp else 15.sp,
            fontWeight = if (isLarge || isBold) FontWeight.Bold else FontWeight.Medium
        )
    }
}
