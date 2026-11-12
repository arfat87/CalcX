package com.calcx.calculator.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.calcx.calculator.core.database.dao.CalculationHistoryDao
import com.calcx.calculator.core.database.dao.ConversionHistoryDao
import com.calcx.calculator.core.database.dao.FavoriteFormulaDao
import com.calcx.calculator.core.database.entity.CalculationHistoryEntity
import com.calcx.calculator.core.database.entity.ConversionHistoryEntity
import com.calcx.calculator.core.database.entity.FavoriteFormulaEntity

@Database(
    entities = [
        CalculationHistoryEntity::class,
        FavoriteFormulaEntity::class,
        ConversionHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CalcXDatabase : RoomDatabase() {
    abstract fun calculationHistoryDao(): CalculationHistoryDao
    abstract fun favoriteFormulaDao(): FavoriteFormulaDao
    abstract fun conversionHistoryDao(): ConversionHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CalcXDatabase? = null

        fun getDatabase(context: Context): CalcXDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CalcXDatabase::class.java,
                    "calcx_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
