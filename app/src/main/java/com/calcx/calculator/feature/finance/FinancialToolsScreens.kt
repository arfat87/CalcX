package com.calcx.calculator.feature.finance

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.calcx.calculator.domain.finance.FinanceEngine

// --- 1. SIMPLE & COMPOUND INTEREST SCREEN ---
@Composable
fun SimpleCompoundInterestScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var principalText by remember { mutableStateOf("10000") }
    var rateText by remember { mutableStateOf("7.5") }
    var timeText by remember { mutableStateOf("3") }
    var isCompound by remember { mutableStateOf(true) }
    var compoundingFrequency by remember { mutableIntStateOf(12) } // Monthly default

    val principal = principalText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val time = timeText.toDoubleOrNull() ?: 0.0

    val result = remember(principal, rate, time, isCompound, compoundingFrequency) {
        if (isCompound) {
            FinanceEngine.calculateCompoundInterest(principal, rate, time, compoundingFrequency)
        } else {
            FinanceEngine.calculateSimpleInterest(principal, rate, time)
        }
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Interest Calculator", onBackClick = onBackClick) },
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = isCompound,
                    onClick = { isCompound = true },
                    label = { Text("Compound Interest") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                )
                FilterChip(
                    selected = !isCompound,
                    onClick = { isCompound = false },
                    label = { Text("Simple Interest") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            OutlinedTextField(
                value = principalText,
                onValueChange = { principalText = it },
                label = { Text("Principal Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rateText,
                onValueChange = { rateText = it },
                label = { Text("Annual Interest Rate (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = timeText,
                onValueChange = { timeText = it },
                label = { Text("Time Period (Years)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Total Interest Earned", color = colors.textSecondary, fontSize = 13.sp)
                Text("₹${result.interestAmount}", color = colors.accent, fontSize = 32.sp, fontWeight = FontWeight.Bold)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Maturity Amount:", color = colors.textSecondary)
                    Text("₹${result.totalAmount}", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- 2. SIP CALCULATOR SCREEN ---
@Composable
fun SipCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var monthlyText by remember { mutableStateOf("5000") }
    var rateText by remember { mutableStateOf("12.0") }
    var yearsText by remember { mutableStateOf("10") }

    val monthly = monthlyText.toDoubleOrNull() ?: 0.0
    val rate = rateText.toDoubleOrNull() ?: 0.0
    val years = yearsText.toDoubleOrNull() ?: 0.0

    val sipResult = remember(monthly, rate, years) {
        FinanceEngine.calculateSip(monthly, rate, years)
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "SIP Calculator", onBackClick = onBackClick) },
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
                value = monthlyText,
                onValueChange = { monthlyText = it },
                label = { Text("Monthly Investment (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = rateText,
                onValueChange = { rateText = it },
                label = { Text("Expected Annual Return (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = yearsText,
                onValueChange = { yearsText = it },
                label = { Text("Investment Duration (Years)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Expected Maturity Amount", color = colors.textSecondary, fontSize = 13.sp)
                Text("₹${sipResult.maturityAmount}", color = Color(0xFF30D158), fontSize = 32.sp, fontWeight = FontWeight.Bold)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Amount Invested:", color = colors.textSecondary)
                    Text("₹${sipResult.totalInvested}", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Estimated Wealth Gain:", color = colors.textSecondary)
                    Text("₹${sipResult.estimatedReturns}", color = colors.accent, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "*Projections are mathematical estimates, not guaranteed market returns.",
                    color = colors.textSecondary.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

// --- 3. DISCOUNT CALCULATOR SCREEN ---
@Composable
fun DiscountCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var priceText by remember { mutableStateOf("2499") }
    var discountText by remember { mutableStateOf("30") }
    var extraDiscountText by remember { mutableStateOf("5") }

    val price = priceText.toDoubleOrNull() ?: 0.0
    val discount = discountText.toDoubleOrNull() ?: 0.0
    val extraDiscount = extraDiscountText.toDoubleOrNull() ?: 0.0

    val result = remember(price, discount, extraDiscount) {
        FinanceEngine.calculateDiscount(price, discount, extraDiscount)
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Discount Calculator", onBackClick = onBackClick) },
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
                value = priceText,
                onValueChange = { priceText = it },
                label = { Text("Original Price (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = discountText,
                onValueChange = { discountText = it },
                label = { Text("Discount (%)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = extraDiscountText,
                onValueChange = { extraDiscountText = it },
                label = { Text("Additional Discount % (Optional)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Final Discounted Price", color = colors.textSecondary, fontSize = 13.sp)
                Text("₹${result.finalPrice}", color = colors.accent, fontSize = 34.sp, fontWeight = FontWeight.Bold)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total You Save:", color = colors.textSecondary)
                    Text("₹${result.amountSaved}", color = Color(0xFF30D158), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }
    }
}

// --- 4. PROFIT & LOSS SCREEN ---
@Composable
fun ProfitLossScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var costText by remember { mutableStateOf("1000") }
    var sellText by remember { mutableStateOf("1250") }

    val cost = costText.toDoubleOrNull() ?: 0.0
    val sell = sellText.toDoubleOrNull() ?: 0.0

    val result = remember(cost, sell) {
        FinanceEngine.calculateProfitLoss(cost, sell)
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Profit & Loss", onBackClick = onBackClick) },
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
                value = costText,
                onValueChange = { costText = it },
                label = { Text("Cost Price / CP (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sellText,
                onValueChange = { sellText = it },
                label = { Text("Selling Price / SP (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                val badgeColor = if (result.isProfit) Color(0xFF30D158) else Color(0xFFFF453A)
                val statusText = if (result.isProfit) "PROFIT" else "LOSS"

                Text(statusText, color = badgeColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text("₹${result.differenceAmount}", color = badgeColor, fontSize = 34.sp, fontWeight = FontWeight.Bold)

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("$statusText Percentage:", color = colors.textSecondary)
                    Text("${result.percentage}%", color = badgeColor, fontWeight = FontWeight.Bold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Profit Margin (on Selling Price):", color = colors.textSecondary)
                    Text("${result.marginPercent}%", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Markup (on Cost Price):", color = colors.textSecondary)
                    Text("${result.markupPercent}%", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// --- 5. BREAK-EVEN CALCULATOR SCREEN ---
@Composable
fun BreakEvenScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var fixedCostText by remember { mutableStateOf("50000") }
    var variableCostText by remember { mutableStateOf("25") }
    var sellPriceText by remember { mutableStateOf("75") }

    val fc = fixedCostText.toDoubleOrNull() ?: 0.0
    val vc = variableCostText.toDoubleOrNull() ?: 0.0
    val sp = sellPriceText.toDoubleOrNull() ?: 0.0

    val result = remember(fc, vc, sp) {
        try {
            FinanceEngine.calculateBreakEven(fc, vc, sp)
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Break-Even Calculator", onBackClick = onBackClick) },
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
                value = fixedCostText,
                onValueChange = { fixedCostText = it },
                label = { Text("Total Fixed Costs (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = variableCostText,
                onValueChange = { variableCostText = it },
                label = { Text("Variable Cost Per Unit (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = sellPriceText,
                onValueChange = { sellPriceText = it },
                label = { Text("Selling Price Per Unit (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (result != null) {
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text("Break-Even Volume", color = colors.textSecondary, fontSize = 13.sp)
                    Text("${result.breakEvenQuantity} Units", color = colors.accent, fontSize = 32.sp, fontWeight = FontWeight.Bold)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Break-Even Revenue:", color = colors.textSecondary)
                        Text("₹${result.breakEvenRevenue}", color = colors.textPrimary, fontWeight = FontWeight.Bold)
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Contribution Margin / Unit:", color = colors.textSecondary)
                        Text("₹${result.contributionMarginPerUnit}", color = Color(0xFF30D158), fontWeight = FontWeight.SemiBold)
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Contribution Margin Ratio:", color = colors.textSecondary)
                        Text("${result.contributionMarginRatio}%", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
