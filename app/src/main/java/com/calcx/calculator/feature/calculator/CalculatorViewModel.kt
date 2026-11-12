package com.calcx.calculator.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcx.calculator.core.preferences.UserPreferencesRepository
import com.calcx.calculator.data.repository.HistoryRepository
import com.calcx.calculator.engine.AngleMode
import com.calcx.calculator.engine.CalculatorEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalculatorUiState(
    val expression: String = "",
    val result: String = "",
    val previewResult: String = "",
    val angleMode: AngleMode = AngleMode.DEG,
    val isScientificExpanded: Boolean = false,
    val hasMemory: Boolean = false,
    val isError: Boolean = false,
    val hapticEnabled: Boolean = true
)

class CalculatorViewModel(
    private val historyRepository: HistoryRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val engine = CalculatorEngine()

    init {
        viewModelScope.launch {
            preferencesRepository.userPreferencesFlow.collectLatest { prefs ->
                val angle = if (prefs.angleMode == "RAD") AngleMode.RAD else AngleMode.DEG
                engine.angleMode = angle
                engine.decimalPrecision = prefs.decimalPrecision
                engine.thousandsSeparator = prefs.thousandsSeparator

                _uiState.update {
                    it.copy(
                        angleMode = angle,
                        isScientificExpanded = prefs.scientificExpanded,
                        hapticEnabled = prefs.hapticFeedbackEnabled
                    )
                }
            }
        }
    }

    fun onInput(char: String) {
        val currentExpr = _uiState.value.expression
        val currentResult = _uiState.value.result

        // If starting a new expression right after a result, determine if continuing or resetting
        val newExpr = if (currentResult.isNotEmpty() && currentExpr.isEmpty()) {
            if (char in listOf("+", "-", "×", "÷", "^", "%")) {
                currentResult + char
            } else {
                char
            }
        } else {
            currentExpr + char
        }

        _uiState.update {
            it.copy(
                expression = newExpr,
                result = "",
                isError = false
            )
        }
        updatePreview(newExpr)
    }

    fun onBackspace() {
        val currentExpr = _uiState.value.expression
        if (currentExpr.isNotEmpty()) {
            val updated = currentExpr.dropLast(1)
            _uiState.update {
                it.copy(
                    expression = updated,
                    result = "",
                    isError = false
                )
            }
            updatePreview(updated)
        }
    }

    fun onClear() {
        _uiState.update {
            it.copy(
                expression = "",
                result = "",
                previewResult = "",
                isError = false
            )
        }
    }

    fun onToggleSign() {
        val expr = _uiState.value.expression
        if (expr.isEmpty()) {
            val res = _uiState.value.result
            if (res.isNotEmpty() && res != "0") {
                val toggled = if (res.startsWith("-")) res.drop(1) else "-$res"
                _uiState.update { it.copy(result = toggled) }
            }
            return
        }

        // Wrap or toggle unary minus
        val updated = if (expr.startsWith("-(") && expr.endsWith(")")) {
            expr.removeSurrounding("-(", ")")
        } else if (expr.startsWith("-")) {
            expr.drop(1)
        } else {
            "-($expr)"
        }

        _uiState.update { it.copy(expression = updated) }
        updatePreview(updated)
    }

    fun onEquals() {
        val expr = _uiState.value.expression
        if (expr.isEmpty()) return

        val evaluation = engine.evaluate(expr)
        evaluation.fold(
            onSuccess = { res ->
                val formatted = res.formattedResult
                _uiState.update {
                    it.copy(
                        expression = "",
                        result = formatted,
                        previewResult = "",
                        isError = false
                    )
                }

                viewModelScope.launch {
                    val category = if (_uiState.value.isScientificExpanded) "SCIENTIFIC" else "STANDARD"
                    historyRepository.saveCalculation(expr, formatted, category)
                }
            },
            onFailure = { error ->
                _uiState.update {
                    it.copy(
                        result = error.message ?: "Error",
                        isError = true
                    )
                }
            }
        )
    }

    fun toggleScientific() {
        val newExpanded = !_uiState.value.isScientificExpanded
        _uiState.update { it.copy(isScientificExpanded = newExpanded) }
        viewModelScope.launch {
            preferencesRepository.setScientificExpanded(newExpanded)
        }
    }

    fun toggleAngleMode() {
        val newMode = if (_uiState.value.angleMode == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
        engine.angleMode = newMode
        _uiState.update { it.copy(angleMode = newMode) }
        viewModelScope.launch {
            preferencesRepository.setAngleMode(newMode.name)
        }
        updatePreview(_uiState.value.expression)
    }

    // Memory operations
    fun memoryClear() {
        engine.memory.clear()
        _uiState.update { it.copy(hasMemory = false) }
    }

    fun memoryRecall() {
        if (engine.memory.hasMemory()) {
            val value = engine.memory.recall()
            onInput(value.toString())
        }
    }

    fun memoryAdd() {
        val currentVal = getCurrentNumericValue() ?: return
        engine.memory.add(currentVal)
        _uiState.update { it.copy(hasMemory = true) }
    }

    fun memorySubtract() {
        val currentVal = getCurrentNumericValue() ?: return
        engine.memory.subtract(currentVal)
        _uiState.update { it.copy(hasMemory = true) }
    }

    private fun getCurrentNumericValue(): Double? {
        val res = _uiState.value.result
        if (res.isNotEmpty()) return res.replace(",", "").toDoubleOrNull()
        val expr = _uiState.value.expression
        if (expr.isNotEmpty()) {
            return engine.evaluate(expr).getOrNull()?.result
        }
        return null
    }

    private fun updatePreview(expr: String) {
        if (expr.isEmpty()) {
            _uiState.update { it.copy(previewResult = "") }
            return
        }
        val evaluation = engine.evaluate(expr)
        _uiState.update {
            it.copy(previewResult = evaluation.getOrNull()?.formattedResult ?: "")
        }
    }
}
