package com.calcx.calculator.feature.history

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.calcx.calculator.core.database.entity.CalculationHistoryEntity
import com.calcx.calculator.core.designsystem.CalcXTheme
import com.calcx.calculator.core.designsystem.components.CalcXCard
import com.calcx.calculator.core.designsystem.components.CalcXTopBar
import com.calcx.calculator.data.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val historyItems: StateFlow<List<CalculationHistoryEntity>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allHistory else repository.searchHistory(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFavorite(item: CalculationHistoryEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, !item.isFavorite)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repository.deleteCalculation(id)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    fun exportCsv(context: Context, items: List<CalculationHistoryEntity>) {
        val csvData = repository.exportToCsv(items)
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, csvData)
            type = "text/csv"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Export Calculation History CSV")
        context.startActivity(shareIntent)
    }
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel,
    onBackClick: () -> Unit,
    onSelectCalculation: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = CalcXTheme.colors
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    val historyItems by viewModel.historyItems.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showClearDialog by remember { mutableStateOf(false) }
    val dateFormat = remember { SimpleDateFormat("MMM d, yyyy  h:mm a", Locale.getDefault()) }

    Scaffold(
        topBar = {
            CalcXTopBar(
                title = "History",
                onBackClick = onBackClick,
                actions = {
                    if (historyItems.isNotEmpty()) {
                        IconButton(onClick = { viewModel.exportCsv(context, historyItems) }) {
                            Icon(Icons.Default.Share, contentDescription = "Export CSV", tint = colors.textPrimary)
                        }
                        IconButton(onClick = { showClearDialog = true }) {
                            Icon(Icons.Default.DeleteSweep, contentDescription = "Clear History", tint = Color(0xFFFF453A))
                        }
                    }
                }
            )
        },
        containerColor = colors.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text("Search history expressions...", color = colors.textSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = colors.textSecondary) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surfacePrimary,
                    unfocusedContainerColor = colors.surfacePrimary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            )

            if (historyItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No calculations yet.", color = colors.textSecondary, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(historyItems, key = { it.id }) { item ->
                        CalcXCard(
                            onClick = {
                                onSelectCalculation(item.expression)
                                clipboard.setText(AnnotatedString(item.result))
                                Toast.makeText(context, "Copied result: ${item.result}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateFormat.format(Date(item.createdAt)),
                                    color = colors.textSecondary,
                                    fontSize = 11.sp
                                )

                                Row {
                                    IconButton(onClick = { viewModel.toggleFavorite(item) }) {
                                        Icon(
                                            imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                                            contentDescription = "Favorite",
                                            tint = if (item.isFavorite) Color(0xFFFFD60A) else colors.textSecondary
                                        )
                                    }
                                    IconButton(onClick = { viewModel.deleteItem(item.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = colors.textSecondary
                                        )
                                    }
                                }
                            }

                            Text(
                                text = item.expression,
                                color = colors.textSecondary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "= ${item.result}",
                                color = colors.accent,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (showClearDialog) {
            AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = { Text("Clear All History?") },
                text = { Text("This will permanently remove all calculation records.") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.clearAll()
                        showClearDialog = false
                    }) {
                        Text("Clear All", color = Color(0xFFFF453A), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
