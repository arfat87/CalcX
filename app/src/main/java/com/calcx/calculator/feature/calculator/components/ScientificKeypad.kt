package com.calcx.calculator.feature.calculator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.components.CalcXButton
import com.calcx.calculator.core.designsystem.components.CalcXButtonVariant

@Composable
fun ScientificKeypad(
    onInput: (String) -> Unit,
    onMemoryClear: () -> Unit,
    onMemoryRecall: () -> Unit,
    onMemoryAdd: () -> Unit,
    onMemorySubtract: () -> Unit,
    hapticEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val buttonShape = RoundedCornerShape(14.dp)
    val buttonHeight = 44.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: sin, cos, tan, log, ln
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcXButton(
                text = "sin",
                onClick = { onInput("sin(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "cos",
                onClick = { onInput("cos(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "tan",
                onClick = { onInput("tan(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "log",
                onClick = { onInput("log(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "ln",
                onClick = { onInput("ln(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
        }

        // Row 2: asin, acos, atan, √, ∛
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcXButton(
                text = "asin",
                onClick = { onInput("asin(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 15.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "acos",
                onClick = { onInput("acos(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 15.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "atan",
                onClick = { onInput("atan(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 15.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "√",
                onClick = { onInput("√") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "∛",
                onClick = { onInput("∛") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
        }

        // Row 3: sinh, cosh, tanh, x^y, 1/x
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcXButton(
                text = "sinh",
                onClick = { onInput("sinh(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "cosh",
                onClick = { onInput("cosh(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "tanh",
                onClick = { onInput("tanh(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "xʸ",
                onClick = { onInput("^") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 16.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "1/x",
                onClick = { onInput("1÷") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 15.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
        }

        // Row 4: π, e, !, (, )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcXButton(
                text = "π",
                onClick = { onInput("π") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "e",
                onClick = { onInput("e") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "!",
                onClick = { onInput("!") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "(",
                onClick = { onInput("(") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = ")",
                onClick = { onInput(")") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 18.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
        }

        // Row 5: MC, MR, M+, M-, mod
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalcXButton(
                text = "MC",
                onClick = onMemoryClear,
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "MR",
                onClick = onMemoryRecall,
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "M+",
                onClick = onMemoryAdd,
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "M−",
                onClick = onMemorySubtract,
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
            CalcXButton(
                text = "mod",
                onClick = { onInput(" mod ") },
                variant = CalcXButtonVariant.SCIENTIFIC,
                fontSize = 14.sp,
                shape = buttonShape,
                hapticFeedbackEnabled = hapticEnabled,
                modifier = Modifier.weight(1f).height(buttonHeight)
            )
        }
    }
}
