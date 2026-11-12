package com.calcx.calculator.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.calcx.calculator.core.designsystem.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "calcx_settings")

data class UserPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val accentIndex: Int = 0,
    val hapticFeedbackEnabled: Boolean = true,
    val soundFeedbackEnabled: Boolean = false,
    val decimalPrecision: Int = 8,
    val angleMode: String = "DEG",
    val scientificNotation: Boolean = false,
    val thousandsSeparator: Boolean = true,
    val scientificExpanded: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val ACCENT_INDEX = intPreferencesKey("accent_index")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val SOUND_FEEDBACK = booleanPreferencesKey("sound_feedback")
        val DECIMAL_PRECISION = intPreferencesKey("decimal_precision")
        val ANGLE_MODE = stringPreferencesKey("angle_mode")
        val SCIENTIFIC_NOTATION = booleanPreferencesKey("scientific_notation")
        val THOUSANDS_SEPARATOR = booleanPreferencesKey("thousands_separator")
        val SCIENTIFIC_EXPANDED = booleanPreferencesKey("scientific_expanded")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        val themeString = preferences[PreferencesKeys.THEME_MODE] ?: ThemeMode.SYSTEM.name
        val themeMode = try {
            ThemeMode.valueOf(themeString)
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }

        UserPreferences(
            themeMode = themeMode,
            accentIndex = preferences[PreferencesKeys.ACCENT_INDEX] ?: 0,
            hapticFeedbackEnabled = preferences[PreferencesKeys.HAPTIC_FEEDBACK] ?: true,
            soundFeedbackEnabled = preferences[PreferencesKeys.SOUND_FEEDBACK] ?: false,
            decimalPrecision = preferences[PreferencesKeys.DECIMAL_PRECISION] ?: 8,
            angleMode = preferences[PreferencesKeys.ANGLE_MODE] ?: "DEG",
            scientificNotation = preferences[PreferencesKeys.SCIENTIFIC_NOTATION] ?: false,
            thousandsSeparator = preferences[PreferencesKeys.THOUSANDS_SEPARATOR] ?: true,
            scientificExpanded = preferences[PreferencesKeys.SCIENTIFIC_EXPANDED] ?: false
        )
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = mode.name
        }
    }

    suspend fun setAccentIndex(index: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ACCENT_INDEX] = index
        }
    }

    suspend fun setHapticFeedback(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAPTIC_FEEDBACK] = enabled
        }
    }

    suspend fun setSoundFeedback(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SOUND_FEEDBACK] = enabled
        }
    }

    suspend fun setDecimalPrecision(precision: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DECIMAL_PRECISION] = precision
        }
    }

    suspend fun setAngleMode(mode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ANGLE_MODE] = mode
        }
    }

    suspend fun setThousandsSeparator(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THOUSANDS_SEPARATOR] = enabled
        }
    }

    suspend fun setScientificExpanded(expanded: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SCIENTIFIC_EXPANDED] = expanded
        }
    }
}
