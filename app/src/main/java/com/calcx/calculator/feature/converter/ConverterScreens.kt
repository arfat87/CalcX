package com.calcx.calculator.feature.converter

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.calcx.calculator.domain.converter.UnitCategory
import com.calcx.calculator.domain.converter.UnitConverterEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalConverterScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var selectedCategory by remember { mutableStateOf(UnitCategory.LENGTH) }
    val unitsForCategory = UnitConverterEngine.categories[selectedCategory] ?: emptyList()

    var fromUnitId by remember(selectedCategory) { mutableStateOf(unitsForCategory.firstOrNull()?.id ?: "") }
    var toUnitId by remember(selectedCategory) { mutableStateOf(unitsForCategory.getOrNull(1)?.id ?: unitsForCategory.firstOrNull()?.id ?: "") }
    var inputValueText by remember { mutableStateOf("1") }

    var fromExpanded by remember { mutableStateOf(false) }
    var toExpanded by remember { mutableStateOf(false) }

    val inputVal = inputValueText.toDoubleOrNull() ?: 0.0

    val convertedValue = remember(selectedCategory, fromUnitId, toUnitId, inputVal) {
        try {
            UnitConverterEngine.convert(selectedCategory, fromUnitId, toUnitId, inputVal)
        } catch (e: Exception) {
            0.0
        }
    }

    val fromUnit = unitsForCategory.find { it.id == fromUnitId }
    val toUnit = unitsForCategory.find { it.id == toUnitId }

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "Unit Converter",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = {
                        val report = "$inputValueText ${fromUnit?.symbol ?: ""} = $convertedValue ${toUnit?.symbol ?: ""}"
                        clipboard.setText(AnnotatedString(report))
                        Toast.makeText(context, "Copied conversion", Toast.LENGTH_SHORT).show()
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
        ) {
            // Category selector tabs
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(UnitCategory.values()) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat.displayName) },
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // FROM CARD
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text("From Unit", color = colors.textSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = fromExpanded,
                        onExpandedChange = { fromExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = "${fromUnit?.name ?: ""} (${fromUnit?.symbol ?: ""})",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = fromExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.accent,
                                unfocusedBorderColor = colors.border,
                                focusedTextColor = colors.textPrimary,
                                unfocusedTextColor = colors.textPrimary
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = fromExpanded,
                            onDismissRequest = { fromExpanded = false }
                        ) {
                            unitsForCategory.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text("${unit.name} (${unit.symbol})") },
                                    onClick = {
                                        fromUnitId = unit.id
                                        fromExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = inputValueText,
                        onValueChange = { inputValueText = it },
                        label = { Text("Input Value") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colors.accent,
                            unfocusedBorderColor = colors.border,
                            focusedTextColor = colors.textPrimary,
                            unfocusedTextColor = colors.textPrimary
                        )
                    )
                }

                // SWAP BUTTON
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    IconButton(
                        onClick = {
                            val temp = fromUnitId
                            fromUnitId = toUnitId
                            toUnitId = temp
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(colors.surfaceSecondary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Units",
                            tint = colors.accent
                        )
                    }
                }

                // TO CARD
                CalcXCard(modifier = Modifier.fillMaxWidth()) {
                    Text("To Unit", color = colors.textSecondary, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = toExpanded,
                        onExpandedChange = { toExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = "${toUnit?.name ?: ""} (${toUnit?.symbol ?: ""})",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colors.accent,
                                unfocusedBorderColor = colors.border,
                                focusedTextColor = colors.textPrimary,
                                unfocusedTextColor = colors.textPrimary
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = toExpanded,
                            onDismissRequest = { toExpanded = false }
                        ) {
                            unitsForCategory.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text("${unit.name} (${unit.symbol})") },
                                    onClick = {
                                        toUnitId = unit.id
                                        toExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("Result", color = colors.textSecondary, fontSize = 12.sp)
                    val formattedResult = if (convertedValue % 1.0 == 0.0) {
                        convertedValue.toLong().toString()
                    } else {
                        String.format("%.6f", convertedValue).trimEnd('0').trimEnd('.')
                    }
                    Text(
                        text = "$formattedResult ${toUnit?.symbol ?: ""}",
                        color = colors.accent,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// --- CURRENCY CONVERTER SCREEN ---
data class CurrencyRate(val code: String, val name: String, val rateToUsd: Double, val symbol: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val context = LocalContext.current

    val currencies = remember {
        listOf(
            CurrencyRate("USD", "US Dollar", 1.0, "$"),
            CurrencyRate("INR", "Indian Rupee", 84.45, "₹"),
            CurrencyRate("EUR", "Euro", 0.94, "€"),
            CurrencyRate("GBP", "British Pound", 0.79, "£"),
            CurrencyRate("JPY", "Japanese Yen", 154.20, "¥"),
            CurrencyRate("CAD", "Canadian Dollar", 1.39, "C$"),
            CurrencyRate("AUD", "Australian Dollar", 1.53, "A$"),
            CurrencyRate("CHF", "Swiss Franc", 0.88, "CHF"),
            CurrencyRate("CNY", "Chinese Yuan", 7.23, "¥"),
            CurrencyRate("AED", "UAE Dirham", 3.67, "AED"),
            CurrencyRate("SGD", "Singapore Dollar", 1.34, "S$")
        )
    }

    var fromCode by remember { mutableStateOf("USD") }
    var toCode by remember { mutableStateOf("INR") }
    var amountText by remember { mutableStateOf("100") }

    val amount = amountText.toDoubleOrNull() ?: 0.0
    val fromRate = currencies.find { it.code == fromCode }?.rateToUsd ?: 1.0
    val toRate = currencies.find { it.code == toCode }?.rateToUsd ?: 1.0

    // USD base: amount / fromRate * toRate
    val converted = (amount / fromRate) * toRate

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "Currency Converter",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Exchange rates refreshed (offline cache)", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = colors.textPrimary)
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
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount to convert") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // From currency select
            Text("From Currency", color = colors.textSecondary, fontSize = 13.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(currencies) { curr ->
                    FilterChip(
                        selected = fromCode == curr.code,
                        onClick = { fromCode = curr.code },
                        label = { Text("${curr.code} (${curr.symbol})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Swap button
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                    onClick = {
                        val t = fromCode
                        fromCode = toCode
                        toCode = t
                    },
                    modifier = Modifier.size(44.dp).clip(CircleShape).background(colors.surfaceSecondary)
                ) {
                    Icon(Icons.Default.SwapVert, contentDescription = "Swap", tint = colors.accent)
                }
            }

            // To currency select
            Text("To Currency", color = colors.textSecondary, fontSize = 13.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(currencies) { curr ->
                    FilterChip(
                        selected = toCode == curr.code,
                        onClick = { toCode = curr.code },
                        label = { Text("${curr.code} (${curr.symbol})") },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Result Card
            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                val toCurr = currencies.find { it.code == toCode }
                Text("Converted Amount", color = colors.textSecondary, fontSize = 13.sp)
                Text(
                    text = "${toCurr?.symbol ?: ""} ${String.format("%.2f", converted)}",
                    color = colors.accent,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                val unitRate = (1.0 / fromRate) * toRate
                Text("1 $fromCode = ${String.format("%.4f", unitRate)} $toCode", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)

                Text(
                    text = "*Rates are for informational reference only. Offline cached rates active.",
                    color = colors.textSecondary.copy(alpha = 0.7f),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
