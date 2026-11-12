package com.calcx.calculator.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.calcx.calculator.core.designsystem.components.BottomBarDestination
import com.calcx.calculator.core.designsystem.components.CalcXBottomBar
import com.calcx.calculator.core.preferences.UserPreferencesRepository
import com.calcx.calculator.data.repository.FavoritesRepository
import com.calcx.calculator.data.repository.HistoryRepository
import com.calcx.calculator.feature.advancedmath.EquationSolverScreen
import com.calcx.calculator.feature.advancedmath.FractionsScreen
import com.calcx.calculator.feature.advancedmath.MatrixCalculatorScreen
import com.calcx.calculator.feature.advancedmath.StatisticsScreen
import com.calcx.calculator.feature.calculator.CalculatorScreen
import com.calcx.calculator.feature.calculator.CalculatorViewModel
import com.calcx.calculator.feature.converter.CurrencyConverterScreen
import com.calcx.calculator.feature.converter.UniversalConverterScreen
import com.calcx.calculator.feature.favorites.FavoritesScreen
import com.calcx.calculator.feature.favorites.FavoritesViewModel
import com.calcx.calculator.feature.finance.BreakEvenScreen
import com.calcx.calculator.feature.finance.DiscountCalculatorScreen
import com.calcx.calculator.feature.finance.EmiCalculatorScreen
import com.calcx.calculator.feature.finance.GstCalculatorScreen
import com.calcx.calculator.feature.finance.ProfitLossScreen
import com.calcx.calculator.feature.finance.SimpleCompoundInterestScreen
import com.calcx.calculator.feature.finance.SipCalculatorScreen
import com.calcx.calculator.feature.graphing.GraphingScreen
import com.calcx.calculator.feature.history.HistoryScreen
import com.calcx.calculator.feature.history.HistoryViewModel
import com.calcx.calculator.feature.programmer.ProgrammerCalculatorScreen
import com.calcx.calculator.feature.settings.AboutScreen
import com.calcx.calculator.feature.settings.PrivacyPolicyScreen
import com.calcx.calculator.feature.settings.SettingsScreen
import com.calcx.calculator.feature.tools.ToolsDashboardScreen

@Composable
fun AppNavHost(
    historyRepository: HistoryRepository,
    favoritesRepository: FavoritesRepository,
    preferencesRepository: UserPreferencesRepository,
    calculatorViewModel: CalculatorViewModel,
    historyViewModel: HistoryViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Calculator.route

    val rootTabs = listOf(
        Screen.Calculator.route,
        Screen.Tools.route,
        Screen.History.route,
        Screen.Settings.route
    )
    val showBottomBar = currentRoute in rootTabs

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CalcXBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { destination ->
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Calculator.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab 1: Main Calculator
            composable(Screen.Calculator.route) {
                CalculatorScreen(
                    viewModel = calculatorViewModel,
                    onNavigateToHistory = { navController.navigate(Screen.History.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }

            // Tab 2: Tools Dashboard
            composable(Screen.Tools.route) {
                ToolsDashboardScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            // Tab 3: History
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = historyViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSelectCalculation = { expr ->
                        calculatorViewModel.onClear()
                        calculatorViewModel.onInput(expr)
                        navController.navigate(Screen.Calculator.route) {
                            popUpTo(Screen.Calculator.route) { inclusive = true }
                        }
                    }
                )
            }

            // Tab 4: Settings
            composable(Screen.Settings.route) {
                SettingsScreen(
                    preferencesRepository = preferencesRepository,
                    onNavigateToAbout = { navController.navigate(Screen.About.route) },
                    onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) }
                )
            }

            // Sub-destinations: Finance
            composable(Screen.Gst.route) {
                GstCalculatorScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Emi.route) {
                EmiCalculatorScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Interest.route) {
                SimpleCompoundInterestScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Sip.route) {
                SipCalculatorScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Discount.route) {
                DiscountCalculatorScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.ProfitLoss.route) {
                ProfitLossScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.BreakEven.route) {
                BreakEvenScreen(onBackClick = { navController.popBackStack() })
            }

            // Sub-destinations: Converters
            composable(Screen.Converter.route) {
                UniversalConverterScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Currency.route) {
                CurrencyConverterScreen(onBackClick = { navController.popBackStack() })
            }

            // Sub-destinations: Programmer
            composable(Screen.Programmer.route) {
                ProgrammerCalculatorScreen(onBackClick = { navController.popBackStack() })
            }

            // Sub-destinations: Advanced Math
            composable(Screen.Statistics.route) {
                StatisticsScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Fractions.route) {
                FractionsScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Matrix.route) {
                MatrixCalculatorScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.EquationSolver.route) {
                EquationSolverScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.Graphing.route) {
                GraphingScreen(onBackClick = { navController.popBackStack() })
            }

            // Sub-destinations: Favorites
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onBackClick = { navController.popBackStack() },
                    onSelectFormula = { formula ->
                        calculatorViewModel.onClear()
                        calculatorViewModel.onInput(formula)
                        navController.navigate(Screen.Calculator.route) {
                            popUpTo(Screen.Calculator.route) { inclusive = true }
                        }
                    }
                )
            }

            // Sub-destinations: Settings About & Privacy
            composable(Screen.About.route) {
                AboutScreen(onBackClick = { navController.popBackStack() })
            }
            composable(Screen.PrivacyPolicy.route) {
                PrivacyPolicyScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
