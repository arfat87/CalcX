# CalcX — Database & Persistence Specification

## 1. Local Persistence Overview

CalcX uses **Room SQLite** for persistent calculation history, favorite formulas, and conversion history, alongside **Jetpack DataStore** for user preferences.

---

## 2. Room Database Entities

### 2.1 `CalculationHistoryEntity`
```kotlin
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
```

### 2.2 `FavoriteFormulaEntity`
```kotlin
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
```

### 2.3 `ConversionHistoryEntity`
```kotlin
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
```

---

## 3. Data Access Objects (DAOs)

- **`CalculationHistoryDao`**:
  - `getAllHistory(): Flow<List<CalculationHistoryEntity>>`
  - `searchHistory(query: String): Flow<List<CalculationHistoryEntity>>`
  - `insert(entity: CalculationHistoryEntity): Long`
  - `updateFavorite(id: Long, isFavorite: Boolean)`
  - `deleteById(id: Long)`
  - `clearAll()`

- **`FavoriteFormulaDao`**:
  - `getAllFavorites(): Flow<List<FavoriteFormulaEntity>>`
  - `insert(formula: FavoriteFormulaEntity): Long`
  - `deleteById(id: Long)`

- **`ConversionHistoryDao`**:
  - `getRecentConversions(): Flow<List<ConversionHistoryEntity>>`
  - `insert(conversion: ConversionHistoryEntity): Long`
  - `clearAll()`

---

## 4. Jetpack DataStore Preferences

Stored in `calcx_settings.preferences_pb`:
- `THEME_MODE`: `SYSTEM`, `DARK`, `LIGHT`
- `ACCENT_INDEX`: 0 (Orange), 1 (Blue), 2 (Green), 3 (Red), 4 (Purple)
- `HAPTIC_FEEDBACK`: Boolean (default `true`)
- `SOUND_FEEDBACK`: Boolean (default `false`)
- `DECIMAL_PRECISION`: Int (default `8`)
- `ANGLE_MODE`: String (`DEG` or `RAD`)
- `THOUSANDS_SEPARATOR`: Boolean (default `true`)
- `SCIENTIFIC_EXPANDED`: Boolean (default `false`)
