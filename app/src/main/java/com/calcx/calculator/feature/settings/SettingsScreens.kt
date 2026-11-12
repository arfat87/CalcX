package com.calcx.calculator.feature.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXBlue
import com.calcx.calculator.core.designsystem.CalcXGreen
import com.calcx.calculator.core.designsystem.CalcXOrange
import com.calcx.calculator.core.designsystem.CalcXPurple
import com.calcx.calculator.core.designsystem.CalcXRed
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.ThemeMode
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.core.preferences.UserPreferences
import com.calcx.calculator.core.preferences.UserPreferencesRepository
import kotlinx.coroutines.launch

val accentColorsList = listOf(
    CalcXOrange,
    CalcXBlue,
    CalcXGreen,
    CalcXRed,
    CalcXPurple
)

@Composable
fun SettingsScreen(
    preferencesRepository: UserPreferencesRepository,
    onNavigateToAbout: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val scope = rememberCoroutineScope()
    val prefs by preferencesRepository.userPreferencesFlow.collectAsState(initial = UserPreferences())

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "Settings",
                onBackClick = null
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
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. APPEARANCE
            Text("APPEARANCE", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Theme", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        ThemeMode.SYSTEM to "System",
                        ThemeMode.DARK to "Dark",
                        ThemeMode.LIGHT to "Light"
                    ).forEach { (mode, title) ->
                        FilterChip(
                            selected = prefs.themeMode == mode,
                            onClick = { scope.launch { preferencesRepository.setThemeMode(mode) } },
                            label = { Text(title) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colors.accent,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = colors.border)

                Text("Accent Color", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    accentColorsList.forEachIndexed { index, color ->
                        val isSelected = prefs.accentIndex == index
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { scope.launch { preferencesRepository.setAccentIndex(index) } },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 2. CALCULATION BEHAVIOR
            Text("CALCULATION BEHAVIOR", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                // Decimal Precision Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Decimal Precision", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                    Text("${prefs.decimalPrecision} digits", color = colors.accent, fontWeight = FontWeight.Bold)
                }

                Slider(
                    value = prefs.decimalPrecision.toFloat(),
                    onValueChange = { scope.launch { preferencesRepository.setDecimalPrecision(it.toInt()) } },
                    valueRange = 2f..12f,
                    steps = 9,
                    colors = SliderDefaults.colors(
                        thumbColor = colors.accent,
                        activeTrackColor = colors.accent,
                        inactiveTrackColor = colors.surfaceSecondary
                    )
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                // Thousands separator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Thousands Separator", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text("e.g. 1,000,000 instead of 1000000", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = prefs.thousandsSeparator,
                        onCheckedChange = { scope.launch { preferencesRepository.setThousandsSeparator(it) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colors.accent)
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                // Angle Mode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Default Angle Mode", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text(if (prefs.angleMode == "DEG") "Degrees (°)" else "Radians (rad)", color = colors.textSecondary, fontSize = 12.sp)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = prefs.angleMode == "DEG",
                            onClick = { scope.launch { preferencesRepository.setAngleMode("DEG") } },
                            label = { Text("DEG") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        )
                        FilterChip(
                            selected = prefs.angleMode == "RAD",
                            onClick = { scope.launch { preferencesRepository.setAngleMode("RAD") } },
                            label = { Text("RAD") },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colors.accent, selectedLabelColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // 3. FEEDBACK & ACCESSIBILITY
            Text("FEEDBACK", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Haptic Feedback", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                        Text("Vibration on button presses", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Switch(
                        checked = prefs.hapticFeedbackEnabled,
                        onCheckedChange = { scope.launch { preferencesRepository.setHapticFeedback(it) } },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = colors.accent)
                    )
                }
            }

            // 4. ABOUT & LEGAL
            Text("ABOUT & LEGAL", color = colors.accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAbout() }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = colors.accent)
                        Text("About CalcX", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(16.dp))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = colors.border)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPrivacy() }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = colors.accent)
                        Text("Privacy Policy", color = colors.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = colors.textSecondary, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// --- ABOUT SCREEN ---
@Composable
fun AboutScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    Scaffold(
        topBar = { CalcXTopBar(title = "About CalcX", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // CalcX Logo Symbol
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(colors.surfacePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text("X", color = colors.accent, fontSize = 48.sp, fontWeight = FontWeight.Black)
            }

            Text("CalcX", color = colors.textPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Beyond Basic Calculations.", color = colors.accent, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text("Version 1.0.0 (Production Release)", color = colors.textSecondary, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(8.dp))

            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Product Identity & Vision", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "CalcX combines the fluid simplicity of modern mobile calculators with the rigorous functionality of a professional scientific, financial, and mathematical engine.",
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = colors.border)

                Text("Completely Free Guarantee", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "CalcX is 100% free with no subscriptions, no ads, no trackers, and no paywalls. All mathematical, financial, converter, and programmer tools are fully unlocked forever.",
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

// --- PRIVACY POLICY SCREEN ---
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    Scaffold(
        topBar = { CalcXTopBar(title = "Privacy Policy", onBackClick = onBackClick) },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CalcXCard(modifier = Modifier.fillMaxWidth()) {
                Text("Privacy First Architecture", color = colors.accent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Your privacy is fundamental to CalcX. We adhere to strict zero-knowledge principles:\n\n" +
                    "1. Local Storage Only: All your calculations, history, formulas, and conversions remain stored strictly on your local device in an encrypted Room SQLite database.\n\n" +
                    "2. No Tracking: CalcX contains zero advertising libraries, zero user tracking SDKs, and zero telemetry monitors.\n\n" +
                    "3. No Cloud Accounts Required: You never need to sign up, provide an email address, or maintain an online account to access any feature.\n\n" +
                    "4. Network Transparency: Network access is utilized exclusively for optional currency rate refreshes and does not transmit any identifiable personal or calculation data.",
                    color = colors.textPrimary,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
