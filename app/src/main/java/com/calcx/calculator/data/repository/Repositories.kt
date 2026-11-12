package com.calcx.calculator.data.repository

import com.calcx.calculator.core.database.dao.CalculationHistoryDao
import com.calcx.calculator.core.database.dao.ConversionHistoryDao
import com.calcx.calculator.core.database.dao.FavoriteFormulaDao
import com.calcx.calculator.core.database.entity.CalculationHistoryEntity
import com.calcx.calculator.core.database.entity.ConversionHistoryEntity
import com.calcx.calculator.core.database.entity.FavoriteFormulaEntity
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryRepository(private val dao: CalculationHistoryDao) {
    val allHistory: Flow<List<CalculationHistoryEntity>> = dao.getAllHistory()
    val favoriteHistory: Flow<List<CalculationHistoryEntity>> = dao.getFavoriteHistory()

    fun searchHistory(query: String): Flow<List<CalculationHistoryEntity>> = dao.searchHistory(query)

    suspend fun saveCalculation(expression: String, result: String, category: String = "STANDARD") {
        if (expression.isBlank() || result.isBlank()) return
        val entity = CalculationHistoryEntity(
            expression = expression,
            result = result,
            category = category,
            createdAt = System.currentTimeMillis()
        )
        dao.insert(entity)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }

    suspend fun deleteCalculation(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    fun exportToCsv(items: List<CalculationHistoryEntity>): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val sb = StringBuilder()
        sb.append("ID,Category,Expression,Result,Date,IsFavorite\n")
        items.forEach { item ->
            val dateStr = dateFormat.format(Date(item.createdAt))
            val cleanExpr = item.expression.replace("\"", "\"\"")
            val cleanRes = item.result.replace("\"", "\"\"")
            sb.append("${item.id},${item.category},\"$cleanExpr\",\"$cleanRes\",$dateStr,${item.isFavorite}\n")
        }
        return sb.toString()
    }
}

class FavoritesRepository(private val dao: FavoriteFormulaDao) {
    val allFavorites: Flow<List<FavoriteFormulaEntity>> = dao.getAllFavorites()

    suspend fun addFavorite(title: String, expression: String, description: String = "", category: String = "GENERAL") {
        val entity = FavoriteFormulaEntity(
            title = title,
            expression = expression,
            description = description,
            category = category
        )
        dao.insert(entity)
    }

    suspend fun deleteFavorite(id: Long) {
        dao.deleteById(id)
    }
}

class ConversionRepository(private val dao: ConversionHistoryDao) {
    val recentConversions: Flow<List<ConversionHistoryEntity>> = dao.getRecentConversions()

    suspend fun saveConversion(category: String, fromUnit: String, toUnit: String, input: Double, output: Double) {
        val entity = ConversionHistoryEntity(
            category = category,
            fromUnit = fromUnit,
            toUnit = toUnit,
            inputValue = input,
            outputValue = output
        )
        dao.insert(entity)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
