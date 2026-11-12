package com.calcx.calculator.navigation

sealed class Screen(val route: String) {
    // Root Tabs
    object Calculator : Screen("calculator")
    object Tools : Screen("tools")
    object History : Screen("history")
    object Settings : Screen("settings")

    // Tool Sub-destinations
    object Gst : Screen("tools/gst")
    object Emi : Screen("tools/emi")
    object Interest : Screen("tools/interest")
    object Sip : Screen("tools/sip")
    object Discount : Screen("tools/discount")
    object ProfitLoss : Screen("tools/profit_loss")
    object BreakEven : Screen("tools/break_even")
    object Converter : Screen("tools/converter")
    object Currency : Screen("tools/currency")
    object Programmer : Screen("tools/programmer")
    object Statistics : Screen("tools/statistics")
    object Fractions : Screen("tools/fractions")
    object Matrix : Screen("tools/matrix")
    object EquationSolver : Screen("tools/equation_solver")
    object Graphing : Screen("tools/graphing")
    object Favorites : Screen("tools/favorites")
    object About : Screen("settings/about")
    object PrivacyPolicy : Screen("settings/privacy")
}
