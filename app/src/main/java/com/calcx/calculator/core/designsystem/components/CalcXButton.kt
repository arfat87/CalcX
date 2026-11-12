package com.calcx.calculator.core.designsystem.components

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme

enum class CalcXButtonVariant {
    NUMBER,
    FUNCTION,
    OPERATOR,
    SCIENTIFIC,
    ACCENT
}

@Composable
fun CalcXButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: CalcXButtonVariant = CalcXButtonVariant.NUMBER,
    isSelected: Boolean = false,
    fontSize: TextUnit = 28.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    shape: Shape = CircleShape,
    hapticFeedbackEnabled: Boolean = true,
    content: (@Composable () -> Unit)? = null
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        label = "button_scale"
    )

    val colors = CalcXTheme.colors

    val (backgroundColor, textColor) = when (variant) {
        CalcXButtonVariant.NUMBER -> Pair(colors.keyNumberBg, colors.keyNumberText)
        CalcXButtonVariant.FUNCTION -> Pair(colors.keyFunctionBg, colors.keyFunctionText)
        CalcXButtonVariant.OPERATOR -> {
            if (isSelected) {
                Pair(Color.White, colors.accent)
            } else {
                Pair(colors.keyOperatorBg, colors.keyOperatorText)
            }
        }
        CalcXButtonVariant.SCIENTIFIC -> Pair(colors.surfaceSecondary, colors.textPrimary)
        CalcXButtonVariant.ACCENT -> Pair(colors.accent, Color.White)
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(shape)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (hapticFeedbackEnabled) {
                        view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    }
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            content()
        } else {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        }
    }
}
