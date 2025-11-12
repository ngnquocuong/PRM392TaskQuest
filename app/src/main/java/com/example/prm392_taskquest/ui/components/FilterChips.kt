package com.example.prm392_taskquest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.prm392_taskquest.data.local.entity.Priority

@Composable
fun TaskFilterChips(
    selectedPriority: Priority?,
    onPrioritySelected: (Priority?) -> Unit,
    selectedCategory: Int?,
    onCategorySelected: (Int?) -> Unit,
    onSortSelected: (SortOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // All Tasks chip
            item {
                FilterChip(
                    selected = selectedPriority == null && selectedCategory == null,
                    onClick = {
                        onPrioritySelected(null)
                        onCategorySelected(null)
                    },
                    label = { Text("All") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.List,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }

            // Priority filters
            items(Priority.values().toList()) { priority ->
                FilterChip(
                    selected = selectedPriority == priority,
                    onClick = {
                        onPrioritySelected(if (selectedPriority == priority) null else priority)
                    },
                    label = { Text(priority.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = getPriorityColor(priority).copy(alpha = 0.3f)
                    )
                )
            }

            // Sort button
            item {
                Box {
                    FilterChip(
                        selected = false,
                        onClick = { showSortMenu = true },
                        label = { Text("Sort") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        SortOption.values().forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.displayName) },
                                onClick = {
                                    onSortSelected(option)
                                    showSortMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getPriorityColor(priority: Priority): androidx.compose.ui.graphics.Color {
    return when (priority) {
        Priority.LOW -> MaterialTheme.colorScheme.tertiary
        Priority.MEDIUM -> MaterialTheme.colorScheme.secondary
        Priority.HIGH -> MaterialTheme.colorScheme.primary
        Priority.URGENT -> MaterialTheme.colorScheme.error
    }
}

enum class SortOption(val displayName: String) {
    DUE_DATE_ASC("Due Date (Earliest)"),
    DUE_DATE_DESC("Due Date (Latest)"),
    PRIORITY_HIGH_TO_LOW("Priority (High to Low)"),
    PRIORITY_LOW_TO_HIGH("Priority (Low to High)"),
    XP_HIGH_TO_LOW("XP (High to Low)"),
    XP_LOW_TO_HIGH("XP (Low to High)"),
    CREATED_DATE_DESC("Newest First"),
    CREATED_DATE_ASC("Oldest First")
}
