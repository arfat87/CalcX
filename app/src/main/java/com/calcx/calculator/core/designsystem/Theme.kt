package com.calcx.calculator.core.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class CalcXColors(
    val background: Color,
    val surfacePrimary: Color,
    val surfaceSecondary: Color,
    val surfaceTertiary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val border: Color,
    val accent: Color,
    val keyNumberBg: Color,
    val keyNumberText: Color,
    val keyFunctionBg: Color,
    val keyFunctionText: Color,
    val keyOperatorBg: Color,
    val keyOperatorText: Color,
    val isDark: Boolean
)

val LocalCalcXColors = staticCompositionLocalOf {
    CalcXDarkColors
}

val CalcXDarkColors = CalcXColors(
    background = DarkBackground,
    surfacePrimary = DarkSurfacePrimary,
    surfaceSecondary = DarkSurfaceSecondary,
    surfaceTertiary = DarkSurfaceTertiary,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textTertiary = DarkTextTertiary,
    border = DarkBorder,
    accent = CalcXOrange,
    keyNumberBg = DarkKeyNumber,
    keyNumberText = DarkTextPrimary,
    keyFunctionBg = DarkKeyFunction,
    keyFunctionText = DarkTextPrimary,
    keyOperatorBg = CalcXOrange,
    keyOperatorText = Color.White,
    isDark = true
)

val CalcXLightColors = CalcXColors(
    background = LightBackground,
    surfacePrimary = LightSurfacePrimary,
    surfaceSecondary = LightSurfaceSecondary,
    surfaceTertiary = LightSurfaceTertiary,
    textPrimary = LightTextPrimary,
    textSecondary = LightTextSecondary,
    textTertiary = LightTextTertiary,
    border = LightBorder,
    accent = CalcXOrange,
    keyNumberBg = LightKeyNumber,
    keyNumberText = LightTextPrimary,
    keyFunctionBg = LightKeyFunction,
    keyFunctionText = LightTextPrimary,
    keyOperatorBg = CalcXOrange,
    keyOperatorText = Color.White,
    isDark = false
)

private val DarkMaterialColorScheme = darkColorScheme(
    primary = CalcXOrange,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceSecondary,
    onPrimaryContainer = DarkTextPrimary,
    secondary = CalcXOrange,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurfacePrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceSecondary,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder
)

private val LightMaterialColorScheme = lightColorScheme(
    primary = CalcXOrange,
    onPrimary = Color.White,
    primaryContainer = LightSurfaceSecondary,
    onPrimaryContainer = LightTextPrimary,
    secondary = CalcXOrange,
    onSecondary = Color.White,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurfacePrimary,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceSecondary,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder
)

enum class ThemeMode {
    SYSTEM,
    DARK,
    LIGHT
}

@Composable
fun CalcXTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    accentColor: Color = CalcXOrange,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val calcXColors = if (darkTheme) {
        CalcXDarkColors.copy(accent = accentColor, keyOperatorBg = accentColor)
    } else {
        CalcXLightColors.copy(accent = accentColor, keyOperatorBg = accentColor)
    }

    val materialColorScheme = if (darkTheme) {
        DarkMaterialColorScheme.copy(primary = accentColor)
    } else {
        LightMaterialColorScheme.copy(primary = accentColor)
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = calcXColors.background.toArgb()
            window.navigationBarColor = calcXColors.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    CompositionLocalProvider(LocalCalcXColors provides calcXColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object CalcXTheme {
    val colors: CalcXColors
        @Composable
        get() = LocalCalcXColors.current
}
