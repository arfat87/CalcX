package com.calcx.calculator.core.designsystem.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalcXDisplay(
    expression: String,
    result: String,
    previewResult: String = "",
    angleMode: String = "DEG",
    hasMemory: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()

    // Automatically scroll to end as user types
    LaunchedEffect(expression) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val copyAction = {
        val textToCopy = if (result.isNotEmpty()) result else expression
        if (textToCopy.isNotEmpty()) {
            clipboardManager.setText(AnnotatedString(textToCopy))
            Toast.makeText(context, "Copied: $textToCopy", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = copyAction,
                onLongClick = copyAction
            ),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Status indicators row (DEG/RAD, Memory)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (hasMemory) {
                    Text(
                        text = "M",
                        color = colors.accent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = angleMode,
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            AnimatedVisibility(
                visible = previewResult.isNotEmpty() && previewResult != result,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "= $previewResult",
                    color = colors.textSecondary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Expression line (scrollable)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = if (expression.isEmpty()) "0" else expression,
                color = if (expression.isEmpty()) colors.textSecondary else colors.textPrimary,
                fontSize = if (expression.length > 20) 24.sp else 32.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                maxLines = 1
            )
        }

        // Main Result line
        val resultFontSize = when {
            result.length > 15 -> 36.sp
            result.length > 10 -> 46.sp
            result.length > 7 -> 56.sp
            else -> 64.sp
        }

        Text(
            text = if (result.isNotEmpty()) result else (if (expression.isEmpty()) "0" else ""),
            color = colors.textPrimary,
            fontSize = resultFontSize,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
