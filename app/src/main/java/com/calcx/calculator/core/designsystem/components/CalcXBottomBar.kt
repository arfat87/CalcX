package com.calcx.calculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.calcx.calculator.core.designsystem.CalcXTheme

enum class BottomBarDestination(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    CALCULATOR("calculator", "Calculator", Icons.Default.Calculate),
    TOOLS("tools", "Tools", Icons.Default.GridView),
    HISTORY("history", "History", Icons.Default.History),
    SETTINGS("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun CalcXBottomBar(
    currentRoute: String,
    onNavigate: (BottomBarDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors

    NavigationBar(
        containerColor = colors.background,
        contentColor = colors.textPrimary,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        BottomBarDestination.values().forEach { destination ->
            val isSelected = currentRoute == destination.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colors.accent,
                    selectedTextColor = colors.accent,
                    unselectedIconColor = colors.textSecondary,
                    unselectedTextColor = colors.textSecondary,
                    indicatorColor = colors.surfaceSecondary
                )
            )
        }
    }
}
