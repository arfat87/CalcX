package com.calcx.calculator

import android.app.Application
import com.calcx.calculator.core.database.CalcXDatabase
import com.calcx.calculator.core.preferences.UserPreferencesRepository
import com.calcx.calculator.data.repository.ConversionRepository
import com.calcx.calculator.data.repository.FavoritesRepository
import com.calcx.calculator.data.repository.HistoryRepository

class CalcXApplication : Application() {

    lateinit var database: CalcXDatabase
        private set

    lateinit var historyRepository: HistoryRepository
        private set

    lateinit var favoritesRepository: FavoritesRepository
        private set

    lateinit var conversionRepository: ConversionRepository
        private set

    lateinit var preferencesRepository: UserPreferencesRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = CalcXDatabase.getDatabase(this)
        historyRepository = HistoryRepository(database.calculationHistoryDao())
        favoritesRepository = FavoritesRepository(database.favoriteFormulaDao())
        conversionRepository = ConversionRepository(database.conversionHistoryDao())
        preferencesRepository = UserPreferencesRepository(this)
    }
}
