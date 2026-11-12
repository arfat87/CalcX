package com.calcx.calculator.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calculation_history")
data class CalculationHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val expression: String,
    val result: String,
    val category: String = "STANDARD",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "favorite_formulas")
data class FavoriteFormulaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val expression: String,
    val description: String = "",
    val category: String = "GENERAL",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "conversion_history")
data class ConversionHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val fromUnit: String,
    val toUnit: String,
    val inputValue: Double,
    val outputValue: Double,
    val createdAt: Long = System.currentTimeMillis()
)
