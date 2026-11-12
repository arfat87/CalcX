package com.calcx.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.preferences.UserPreferences
import com.calcx.calculator.feature.calculator.CalculatorViewModel
import com.calcx.calculator.feature.favorites.FavoritesViewModel
import com.calcx.calculator.feature.history.HistoryViewModel
import com.calcx.calculator.feature.settings.accentColorsList
import com.calcx.calculator.navigation.AppNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as CalcXApplication
        val historyRepo = app.historyRepository
        val favoritesRepo = app.favoritesRepository
        val prefsRepo = app.preferencesRepository

        setContent {
            val userPreferences by prefsRepo.userPreferencesFlow.collectAsState(initial = UserPreferences())
            val accentColor = remember(userPreferences.accentIndex) {
                accentColorsList.getOrElse(userPreferences.accentIndex) { accentColorsList[0] }
            }

            val calculatorViewModel = remember { CalculatorViewModel(historyRepo, prefsRepo) }
            val historyViewModel = remember { HistoryViewModel(historyRepo) }
            val favoritesViewModel = remember { FavoritesViewModel(favoritesRepo) }

            CalcXTheme(
                themeMode = userPreferences.themeMode,
                accentColor = accentColor
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CalcXTheme.colors.background
                ) {
                    AppNavHost(
                        historyRepository = historyRepo,
                        favoritesRepository = favoritesRepo,
                        preferencesRepository = prefsRepo,
                        calculatorViewModel = calculatorViewModel,
                        historyViewModel = historyViewModel,
                        favoritesViewModel = favoritesViewModel
                    )
                }
            }
        }
    }
}
