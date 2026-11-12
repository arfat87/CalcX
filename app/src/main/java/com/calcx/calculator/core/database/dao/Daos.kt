package com.calcx.calculator.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.calcx.calculator.core.database.entity.CalculationHistoryEntity
import com.calcx.calculator.core.database.entity.ConversionHistoryEntity
import com.calcx.calculator.core.database.entity.FavoriteFormulaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationHistoryDao {
    @Query("SELECT * FROM calculation_history ORDER BY createdAt DESC")
    fun getAllHistory(): Flow<List<CalculationHistoryEntity>>

    @Query("SELECT * FROM calculation_history WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteHistory(): Flow<List<CalculationHistoryEntity>>

    @Query("SELECT * FROM calculation_history WHERE expression LIKE '%' || :query || '%' OR result LIKE '%' || :query || '%' ORDER BY createdAt DESC")
    fun searchHistory(query: String): Flow<List<CalculationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CalculationHistoryEntity): Long

    @Query("UPDATE calculation_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("DELETE FROM calculation_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM calculation_history")
    suspend fun clearAll()
}

@Dao
interface FavoriteFormulaDao {
    @Query("SELECT * FROM favorite_formulas ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteFormulaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(formula: FavoriteFormulaEntity): Long

    @Update
    suspend fun update(formula: FavoriteFormulaEntity)

    @Query("DELETE FROM favorite_formulas WHERE id = :id")
    suspend fun deleteById(id: Long)
}

@Dao
interface ConversionHistoryDao {
    @Query("SELECT * FROM conversion_history ORDER BY createdAt DESC LIMIT 50")
    fun getRecentConversions(): Flow<List<ConversionHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversion: ConversionHistoryEntity): Long

    @Query("DELETE FROM conversion_history")
    suspend fun clearAll()
}
