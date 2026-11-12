package com.calcx.calculator.feature.programmer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXButton
import com.calcx.calculator.core.designsystem.components.CalcXButtonVariant
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.domain.programmer.BitWidth
import com.calcx.calculator.domain.programmer.NumberBase
import com.calcx.calculator.domain.programmer.ProgrammerEngine
import com.calcx.calculator.domain.programmer.ProgrammerState

@Composable
fun ProgrammerCalculatorScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var activeBase by remember { mutableStateOf(NumberBase.DEC) }
    var bitWidth by remember { mutableStateOf(BitWidth.QWORD) }
    var isSigned by remember { mutableStateOf(true) }
    var rawValue by remember { mutableStateOf(0L) }

    // Pending bitwise operation
    var pendingOp by remember { mutableStateOf<String?>(null) }
    var operandA by remember { mutableStateOf(0L) }

    val state = remember(rawValue, activeBase, bitWidth, isSigned) {
        ProgrammerState(rawValue, activeBase, bitWidth, isSigned)
    }

    fun onDigit(char: Char) {
        val digitVal = char.digitToIntOrNull(activeBase.radix) ?: return
        val current = state.maskedValue()
        val next = (current * activeBase.radix) + digitVal
        rawValue = next and bitWidth.mask
    }

    fun onBackspace() {
        val current = state.maskedValue()
        rawValue = (current ushr when (activeBase) {
            NumberBase.BIN -> 1
            NumberBase.OCT -> 3
            NumberBase.HEX -> 4
            NumberBase.DEC -> return run { rawValue = current / 10 }
        }) and bitWidth.mask
    }

    fun onClear() {
        rawValue = 0L
        pendingOp = null
        operandA = 0L
    }

    fun applyBitwise(op: String) {
        when (op) {
            "NOT" -> {
                rawValue = ProgrammerEngine.bitwiseNot(state.maskedValue(), bitWidth)
            }
            "LSH" -> {
                rawValue = ProgrammerEngine.shiftLeft(state.maskedValue(), 1, bitWidth)
            }
            "RSH" -> {
                rawValue = ProgrammerEngine.shiftRight(state.maskedValue(), 1, bitWidth)
            }
            else -> {
                operandA = state.maskedValue()
                pendingOp = op
                rawValue = 0L
            }
        }
    }

    fun onEquals() {
        val op = pendingOp ?: return
        val b = state.maskedValue()
        rawValue = when (op) {
            "AND" -> ProgrammerEngine.bitwiseAnd(operandA, b, bitWidth)
            "OR" -> ProgrammerEngine.bitwiseOr(operandA, b, bitWidth)
            "XOR" -> ProgrammerEngine.bitwiseXor(operandA, b, bitWidth)
            else -> b
        }
        pendingOp = null
    }

    Scaffold(
        topBar = { CalcXTopBar(title = "Programmer Calculator", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Bit Width & Signed Selectors
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    BitWidth.values().forEach { width ->
                        FilterChip(
                            selected = bitWidth == width,
                            onClick = {
                                bitWidth = width
                                rawValue = rawValue and width.mask
                            },
                            label = { Text(width.name, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                FilterChip(
                    selected = isSigned,
                    onClick = { isSigned = !isSigned },
                    label = { Text(if (isSigned) "Signed" else "Unsigned", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.surfaceSecondary, selectedLabelColor = colors.accent),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Simultaneous Base Values Display Card
            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                NumberBase.values().forEach { base ->
                    val isCurrent = activeBase == base
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isCurrent) colors.accent.copy(alpha = 0.12f) else Color.Transparent)
                            .clickable { activeBase = base }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = base.name,
                            color = if (isCurrent) colors.accent else colors.textSecondary,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                        Text(
                            text = state.toFormattedString(base),
                            color = if (isCurrent) colors.accent else colors.textPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Bitwise operations row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("AND", "OR", "XOR", "NOT", "LSH", "RSH").forEach { op ->
                    val isPending = pendingOp == op
                    CalcXButton(
                        text = op,
                        onClick = { applyBitwise(op) },
                        variant = if (isPending) CalcXButtonVariant.ACCENT else CalcXButtonVariant.SCIENTIFIC,
                        fontSize = 12.sp,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(38.dp)
                    )
                }
            }

            // Dynamic Programmer Keypad based on activeBase
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Row 1: A, B, C, D, E, F
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf('A', 'B', 'C', 'D', 'E', 'F').forEach { char ->
                        val enabled = activeBase == NumberBase.HEX
                        CalcXButton(
                            text = char.toString(),
                            onClick = { if (enabled) onDigit(char) },
                            variant = if (enabled) CalcXButtonVariant.NUMBER else CalcXButtonVariant.SCIENTIFIC,
                            fontSize = 18.sp,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).aspectRatio(1.2f)
                        )
                    }
                }

                // Row 2: 7, 8, 9, AC
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf('7', '8', '9').forEach { digit ->
                        val enabled = digit.digitToInt() < activeBase.radix
                        CalcXButton(
                            text = digit.toString(),
                            onClick = { if (enabled) onDigit(digit) },
                            variant = if (enabled) CalcXButtonVariant.NUMBER else CalcXButtonVariant.SCIENTIFIC,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f).aspectRatio(1.2f)
                        )
                    }
                    CalcXButton(
                        text = "AC",
                        onClick = { onClear() },
                        variant = CalcXButtonVariant.FUNCTION,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f).aspectRatio(1.2f)
                    )
                }

                // Row 3: 4, 5, 6, Backspace
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf('4', '5', '6').forEach { digit ->
                        val enabled = digit.digitToInt() < activeBase.radix
                        CalcXButton(
                            text = digit.toString(),
                            onClick = { if (enabled) onDigit(digit) },
                            variant = if (enabled) CalcXButtonVariant.NUMBER else CalcXButtonVariant.SCIENTIFIC,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f).aspectRatio(1.2f)
                        )
                    }
                    CalcXButton(
                        text = "⌫",
                        onClick = { onBackspace() },
                        variant = CalcXButtonVariant.FUNCTION,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f).aspectRatio(1.2f)
                    )
                }

                // Row 4: 1, 2, 3, =
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf('1', '2', '3').forEach { digit ->
                        val enabled = digit.digitToInt() < activeBase.radix
                        CalcXButton(
                            text = digit.toString(),
                            onClick = { if (enabled) onDigit(digit) },
                            variant = if (enabled) CalcXButtonVariant.NUMBER else CalcXButtonVariant.SCIENTIFIC,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f).aspectRatio(1.2f)
                        )
                    }
                    CalcXButton(
                        text = "=",
                        onClick = { onEquals() },
                        variant = CalcXButtonVariant.OPERATOR,
                        fontSize = 22.sp,
                        modifier = Modifier.weight(1f).aspectRatio(1.2f)
                    )
                }

                // Row 5: 0 (wide)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CalcXButton(
                        text = "0",
                        onClick = { onDigit('0') },
                        variant = CalcXButtonVariant.NUMBER,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )
                }
            }
        }
    }
}
