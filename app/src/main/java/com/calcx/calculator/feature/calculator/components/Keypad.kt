package com.calcx.calculator.feature.calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.components.CalcXButton
import com.calcx.calculator.core.designsystem.components.CalcXButtonVariant

@Composable
fun StandardKeypad(
    onInput: (String) -> Unit,
    onClear: () -> Unit,
    onBackspace: () -> Unit,
    onToggleSign: () -> Unit,
    onEquals: () -> Unit,
    hapticEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: AC, +/-, %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalcXButton(
                text = "AC",
                onClick = onClear,
                variant = CalcXButtonVariant.FUNCTION,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "+/-",
                onClick = onToggleSign,
                variant = CalcXButtonVariant.FUNCTION,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "%",
                onClick = { onInput("%") },
                variant = CalcXButtonVariant.FUNCTION,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "÷",
                onClick = { onInput("÷") },
                variant = CalcXButtonVariant.OPERATOR,
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalcXButton(
                text = "7",
                onClick = { onInput("7") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "8",
                onClick = { onInput("8") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "9",
                onClick = { onInput("9") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "×",
                onClick = { onInput("×") },
                variant = CalcXButtonVariant.OPERATOR,
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalcXButton(
                text = "4",
                onClick = { onInput("4") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "5",
                onClick = { onInput("5") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "6",
                onClick = { onInput("6") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "−",
                onClick = { onInput("−") },
                variant = CalcXButtonVariant.OPERATOR,
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalcXButton(
                text = "1",
                onClick = { onInput("1") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "2",
                onClick = { onInput("2") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "3",
                onClick = { onInput("3") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "+",
                onClick = { onInput("+") },
                variant = CalcXButtonVariant.OPERATOR,
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
        }

        // Row 5: Backspace, 0, ., =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CalcXButton(
                text = "",
                onClick = onBackspace,
                variant = CalcXButtonVariant.NUMBER,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace"
                )
            }
            CalcXButton(
                text = "0",
                onClick = { onInput("0") },
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = ".",
                onClick = { onInput(".") },
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
            CalcXButton(
                text = "=",
                onClick = onEquals,
                variant = CalcXButtonVariant.OPERATOR,
                fontSize = 32.sp,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).aspectRatio(1f)
            )
        }
    }
}
