package com.calcx.calculator.feature.graphing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.engine.AngleMode
import com.calcx.calculator.engine.CalculatorEngine

@Composable
fun GraphingScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    var functionExpr by remember { mutableStateOf("sin(x)") }
    var xRange by remember { mutableDoubleStateOf(10.0) } // Domain: [-xRange, +xRange]
    var yRange by remember { mutableDoubleStateOf(5.0) }

    val engine = remember { CalculatorEngine(angleMode = AngleMode.RAD) }

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "Graphing Calculator",
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = {
                        xRange = 10.0
                        yRange = 5.0
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Zoom", tint = colors.textPrimary)
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Function Expression Input
            OutlinedTextField(
                value = functionExpr,
                onValueChange = { functionExpr = it },
                label = { Text("f(x) = (e.g. sin(x), x^2 - 4, cos(2*x))") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Graph Display Card
            CalcXCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val originX = w / 2f
                        val originY = h / 2f

                        val scaleX = (w / 2f) / xRange.toFloat()
                        val scaleY = (h / 2f) / yRange.toFloat()

                        // 1. Draw Grid Lines
                        val gridColor = colors.border.copy(alpha = 0.4f)
                        val stepX = maxOf(1.0, xRange / 5.0)
                        val stepY = maxOf(1.0, yRange / 5.0)

                        var currX = -xRange
                        while (currX <= xRange) {
                            val screenX = originX + (currX.toFloat() * scaleX)
                            drawLine(
                                color = gridColor,
                                start = Offset(screenX, 0f),
                                end = Offset(screenX, h),
                                strokeWidth = 1f
                            )
                            currX += stepX
                        }

                        var currY = -yRange
                        while (currY <= yRange) {
                            val screenY = originY - (currY.toFloat() * scaleY)
                            drawLine(
                                color = gridColor,
                                start = Offset(0f, screenY),
                                end = Offset(w, screenY),
                                strokeWidth = 1f
                            )
                            currY += stepY
                        }

                        // 2. Draw X & Y Axes
                        val axisColor = colors.textSecondary
                        // X Axis
                        drawLine(
                            color = axisColor,
                            start = Offset(0f, originY),
                            end = Offset(w, originY),
                            strokeWidth = 2f
                        )
                        // Y Axis
                        drawLine(
                            color = axisColor,
                            start = Offset(originX, 0f),
                            end = Offset(originX, h),
                            strokeWidth = 2f
                        )

                        // 3. Plot Function f(x)
                        val sampleCount = 300
                        val path = Path()
                        var firstPoint = true

                        for (i in 0..sampleCount) {
                            val worldX = -xRange + (i.toDouble() / sampleCount.toDouble()) * (2.0 * xRange)
                            val substituted = functionExpr.replace("x", "($worldX)")
                            val eval = engine.evaluate(substituted)

                            if (eval.isSuccess) {
                                val worldY = eval.getOrThrow().result
                                if (!worldY.isNaN() && !worldY.isInfinite() && worldY >= -yRange * 2 && worldY <= yRange * 2) {
                                    val screenX = originX + (worldX.toFloat() * scaleX)
                                    val screenY = originY - (worldY.toFloat() * scaleY)

                                    if (firstPoint) {
                                        path.moveTo(screenX, screenY)
                                        firstPoint = false
                                    } else {
                                        path.lineTo(screenX, screenY)
                                    }
                                } else {
                                    firstPoint = true
                                }
                            } else {
                                firstPoint = true
                            }
                        }

                        drawPath(
                            path = path,
                            color = colors.accent,
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    // Zoom Controls overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (xRange > 1.0) {
                                    xRange *= 0.8
                                    yRange *= 0.8
                                }
                            },
                            modifier = Modifier.background(colors.surfaceSecondary, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Zoom In", tint = colors.textPrimary)
                        }

                        IconButton(
                            onClick = {
                                if (xRange < 100.0) {
                                    xRange *= 1.25
                                    yRange *= 1.25
                                }
                            },
                            modifier = Modifier.background(colors.surfaceSecondary, RoundedCornerShape(8.dp))
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Zoom Out", tint = colors.textPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Viewing Domain: [ -${String.format("%.1f", xRange)}, +${String.format("%.1f", xRange)} ]  |  Range: [ -${String.format("%.1f", yRange)}, +${String.format("%.1f", yRange)} ]",
                color = colors.textSecondary,
                fontSize = 12.sp
            )
        }
    }
}
