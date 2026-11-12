package com.calcx.calculator.feature.tools

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.navigation.Screen

enum class ToolCategory(val title: String) {
    ALL("All Tools"),
    FINANCE("Finance"),
    MATHEMATICS("Mathematics"),
    CONVERTERS("Converters"),
    PROGRAMMER("Programmer")
}

data class ToolItem(
    val title: String,
    val description: String,
    val category: ToolCategory,
    val icon: ImageVector,
    val accentColor: Color,
    val route: String
)

@Composable
fun ToolsDashboardScreen(
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ToolCategory.ALL) }

    val allTools = remember {
        listOf(
            // Finance
            ToolItem("GST Calculator", "Calculate CGST, SGST, IGST inclusive & exclusive", ToolCategory.FINANCE, Icons.Default.ReceiptLong, Color(0xFFFF9F0A), Screen.Gst.route),
            ToolItem("EMI Calculator", "Loan payments, total interest & amortization schedule", ToolCategory.FINANCE, Icons.Default.AccountBalance, Color(0xFF0A84FF), Screen.Emi.route),
            ToolItem("Simple & Compound Interest", "Interest growth with custom compounding periods", ToolCategory.FINANCE, Icons.Default.ShowChart, Color(0xFF30D158), Screen.Interest.route),
            ToolItem("SIP Calculator", "Systematic Investment Plan returns and wealth projections", ToolCategory.FINANCE, Icons.Default.Savings, Color(0xFFBF5AF2), Screen.Sip.route),
            ToolItem("Discount Calculator", "Sale discounts, savings and double discounts", ToolCategory.FINANCE, Icons.Default.Discount, Color(0xFFFF453A), Screen.Discount.route),
            ToolItem("Profit & Loss", "Calculate profit/loss margins, markup and percentages", ToolCategory.FINANCE, Icons.Default.Insights, Color(0xFFFF9F0A), Screen.ProfitLoss.route),
            ToolItem("Break-Even", "Fixed costs, variable costs, contribution margin", ToolCategory.FINANCE, Icons.Default.PointOfSale, Color(0xFF0A84FF), Screen.BreakEven.route),

            // Mathematics
            ToolItem("Graphing Calculator", "Interactive 2D Cartesian function plotting with pan & zoom", ToolCategory.MATHEMATICS, Icons.Default.AutoGraph, Color(0xFF0A84FF), Screen.Graphing.route),
            ToolItem("Statistics", "Mean, median, mode, standard deviation, variance & percentiles", ToolCategory.MATHEMATICS, Icons.Default.PieChart, Color(0xFF30D158), Screen.Statistics.route),
            ToolItem("Fractions", "Rational arithmetic, simplification & mixed fractions", ToolCategory.MATHEMATICS, Icons.Default.Calculate, Color(0xFFFF9F0A), Screen.Fractions.route),
            ToolItem("Matrix Calculator", "Matrix addition, multiplication, determinant & inverse", ToolCategory.MATHEMATICS, Icons.Default.ViewAgenda, Color(0xFFBF5AF2), Screen.Matrix.route),
            ToolItem("Equation Solver", "Linear, quadratic and 2-variable systems", ToolCategory.MATHEMATICS, Icons.Default.Functions, Color(0xFFFF453A), Screen.EquationSolver.route),

            // Converters
            ToolItem("Universal Unit Converter", "Length, weight, temperature, area, speed, time, data & more", ToolCategory.CONVERTERS, Icons.Default.Straighten, Color(0xFF30D158), Screen.Converter.route),
            ToolItem("Currency Converter", "Live and offline multi-currency exchange rates", ToolCategory.CONVERTERS, Icons.Default.CurrencyExchange, Color(0xFFFF9F0A), Screen.Currency.route),

            // Programmer
            ToolItem("Programmer Calculator", "HEX, DEC, OCT, BIN conversions & bitwise logic", ToolCategory.PROGRAMMER, Icons.Default.Terminal, Color(0xFF0A84FF), Screen.Programmer.route),

            // Favorites
            ToolItem("Favorite Formulas", "Access saved formulas and custom calculations", ToolCategory.ALL, Icons.Default.Star, Color(0xFFFFD60A), Screen.Favorites.route)
        )
    }

    val filteredTools = allTools.filter { tool ->
        val matchesCategory = selectedCategory == ToolCategory.ALL || tool.category == selectedCategory
        val matchesQuery = searchQuery.isBlank() ||
                tool.title.contains(searchQuery, ignoreCase = true) ||
                tool.description.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Text(
                text = "Tools & Calculators",
                color = colors.textPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Comprehensive math, finance & engineering tools",
                color = colors.textSecondary,
                fontSize = 14.sp
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search tools (e.g. GST, EMI, Unit, Matrix)...", color = colors.textSecondary) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = colors.textSecondary
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accent,
                unfocusedBorderColor = colors.border,
                focusedContainerColor = colors.surfacePrimary,
                unfocusedContainerColor = colors.surfacePrimary,
                focusedTextColor = colors.textPrimary,
                unfocusedTextColor = colors.textPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp)
        )

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ToolCategory.values()) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat.title) },
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

        // Tools List
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredTools) { tool ->
                CalcXCard(
                    onClick = { onNavigate(tool.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(tool.accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = tool.title,
                                tint = tool.accentColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = tool.title,
                                color = colors.textPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = tool.description,
                                color = colors.textSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
