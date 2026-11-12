package com.calcx.calculator.feature.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXDisplay
import com.calcx.calculator.feature.calculator.components.ScientificKeypad
import com.calcx.calculator.feature.calculator.components.StandardKeypad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = CalcXTheme.colors

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CalcX",
                        color = colors.textPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    // Angle Mode Toggle Button (DEG / RAD)
                    TextButton(
                        onClick = { viewModel.toggleAngleMode() }
                    ) {
                        Text(
                            text = uiState.angleMode.name,
                            color = colors.accent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Scientific Mode Toggle Button
                    IconButton(onClick = { viewModel.toggleScientific() }) {
                        Icon(
                            imageVector = Icons.Default.Functions,
                            contentDescription = "Toggle Scientific Keypad",
                            tint = if (uiState.isScientificExpanded) colors.accent else colors.textSecondary
                        )
                    }

                    // History Shortcut
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = colors.textPrimary
                        )
                    }

                    // Settings Shortcut
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = colors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.background,
                    titleContentColor = colors.textPrimary
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main Display (Expression, Preview, Main Result)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                CalcXDisplay(
                    expression = uiState.expression,
                    result = uiState.result,
                    previewResult = uiState.previewResult,
                    angleMode = uiState.angleMode.name,
                    hasMemory = uiState.hasMemory,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Expandable Scientific Keypad
            AnimatedVisibility(
                visible = uiState.isScientificExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ScientificKeypad(
                    onInput = { viewModel.onInput(it) },
                    onMemoryClear = { viewModel.memoryClear() },
                    onMemoryRecall = { viewModel.memoryRecall() },
                    onMemoryAdd = { viewModel.memoryAdd() },
                    onMemorySubtract = { viewModel.memorySubtract() },
                    hapticEnabled = uiState.hapticEnabled
                )
            }

            // Standard Calculator Keypad
            StandardKeypad(
                onInput = { viewModel.onInput(it) },
                onClear = { viewModel.onClear() },
                onBackspace = { viewModel.onBackspace() },
                onToggleSign = { viewModel.onToggleSign() },
                onEquals = { viewModel.onEquals() },
                hapticEnabled = uiState.hapticEnabled,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
