package com.example.prm392_taskquest.ui.screens.task

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392_taskquest.data.local.entity.Priority
import com.example.prm392_taskquest.data.local.entity.RecurringType
import com.example.prm392_taskquest.ui.theme.PRM392TaskQuestTheme
import com.example.prm392_taskquest.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val taskTitle by viewModel.taskTitle.collectAsState()
    val taskDescription by viewModel.taskDescription.collectAsState()
    val taskPriority by viewModel.taskPriority.collectAsState()
    val taskDueDate by viewModel.taskDueDate.collectAsState()
    val taskEstimatedMinutes by viewModel.taskEstimatedMinutes.collectAsState()
    val taskXpReward by viewModel.taskXpReward.collectAsState()
    val isRecurring by viewModel.isRecurring.collectAsState()
    val recurringType by viewModel.recurringType.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }

    // Use the content function with ViewModel state
    AddEditTaskScreenContent(
        taskTitle = taskTitle,
        taskDescription = taskDescription,
        taskPriority = taskPriority,
        taskDueDate = taskDueDate,
        taskEstimatedMinutes = taskEstimatedMinutes,
        taskXpReward = taskXpReward,
        isRecurring = isRecurring,
        recurringType = recurringType,
        onTitleChange = { viewModel.updateTitle(it) },
        onDescriptionChange = { viewModel.updateDescription(it) },
        onPriorityChange = { viewModel.updatePriority(it) },
        onDueDateClick = { showDatePicker = true },
        onEstimatedMinutesChange = { viewModel.updateEstimatedMinutes(it) },
        onXpRewardChange = { viewModel.updateXpReward(it) },
        onRecurringChange = { viewModel.updateRecurring(it) },
        onRecurringTypeChange = { viewModel.updateRecurringType(it) },
        onSave = { viewModel.saveTask(onSuccess = onNavigateBack) },
        onNavigateBack = onNavigateBack
    )

    // Date Picker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateDueDate(millis)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditTaskScreenPreview() {
    PRM392TaskQuestTheme {
        AddEditTaskScreenContent(
            taskTitle = "Complete homework",
            taskDescription = "Finish math assignment chapter 5",
            taskPriority = Priority.HIGH,
            taskDueDate = System.currentTimeMillis(),
            taskEstimatedMinutes = 60,
            taskXpReward = 25,
            isRecurring = false,
            recurringType = null,
            onTitleChange = {},
            onDescriptionChange = {},
            onPriorityChange = {},
            onDueDateClick = {},
            onEstimatedMinutesChange = {},
            onXpRewardChange = {},
            onRecurringChange = {},
            onRecurringTypeChange = {},
            onSave = {},
            onNavigateBack = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditTaskScreenContent(
    taskTitle: String,
    taskDescription: String,
    taskPriority: Priority,
    taskDueDate: Long?,
    taskEstimatedMinutes: Int,
    taskXpReward: Int,
    isRecurring: Boolean,
    recurringType: RecurringType?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onDueDateClick: () -> Unit,
    onEstimatedMinutesChange: (Int) -> Unit,
    onXpRewardChange: (Int) -> Unit,
    onRecurringChange: (Boolean) -> Unit,
    onRecurringTypeChange: (RecurringType?) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSave,
                        enabled = taskTitle.isNotBlank()
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = taskTitle,
                onValueChange = onTitleChange,
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Description
            OutlinedTextField(
                value = taskDescription,
                onValueChange = onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            // Priority Selection
            Column {
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.values().forEach { priority ->
                        FilterChip(
                            selected = taskPriority == priority,
                            onClick = { onPriorityChange(priority) },
                            label = { Text(priority.name) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Due Date
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDueDateClick
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Due Date",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = if (taskDueDate != null) {
                                dateFormat.format(Date(taskDueDate))
                            } else {
                                "Not set"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select Date"
                    )
                }
            }

            // Estimated Time
            Column {
                Text(
                    text = "Estimated Time: $taskEstimatedMinutes minutes",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = taskEstimatedMinutes.toFloat(),
                    onValueChange = { onEstimatedMinutesChange(it.toInt()) },
                    valueRange = 15f..240f,
                    steps = 14,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "15 min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "4 hours",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // XP Reward
            OutlinedTextField(
                value = taskXpReward.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { xp -> onXpRewardChange(xp) }
                },
                label = { Text("XP Reward") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(Icons.Default.Star, contentDescription = null)
                },
                singleLine = true
            )

            // Recurring
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recurring Task",
                    style = MaterialTheme.typography.titleSmall
                )
                Switch(
                    checked = isRecurring,
                    onCheckedChange = onRecurringChange
                )
            }

            if (isRecurring) {
                Column {
                    Text(
                        text = "Repeat",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RecurringType.values().forEach { type ->
                            FilterChip(
                                selected = recurringType == type,
                                onClick = { onRecurringTypeChange(type) },
                                label = { Text(type.name) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
