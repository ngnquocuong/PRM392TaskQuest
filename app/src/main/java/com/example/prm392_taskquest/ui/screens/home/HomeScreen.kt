package com.example.prm392_taskquest.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392_taskquest.data.local.entity.Priority
import com.example.prm392_taskquest.ui.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNavigateToAddTask: () -> Unit,
    onNavigateToTaskList: () -> Unit,
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAchievements: () -> Unit = {}
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val activeTasks by viewModel.activeTasks.collectAsState()
    val todayQuests by viewModel.todayQuests.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Quest") },
                actions = {
                    IconButton(onClick = onNavigateToAchievements) {
                        Icon(Icons.Default.Star, contentDescription = "Achievements")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /* Already on home */ }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                            Text("Home", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = onNavigateToTaskList) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.List, contentDescription = "Tasks")
                            Text("Tasks", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = onNavigateToStatistics) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Info, contentDescription = "Stats")
                            Text("Stats", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Person, contentDescription = "Profile")
                            Text("Profile", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Profile Card
            item {
                userProfile?.let { profile ->
                    UserProfileCard(
                        level = profile.level,
                        xp = profile.xp,
                        currentStreak = profile.currentStreak,
                        totalTasksCompleted = profile.totalTasksCompleted
                    )
                }
            }

            // Daily Quests
            if (todayQuests.isNotEmpty()) {
                item {
                    Text(
                        text = "Today's Quests",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                items(todayQuests) { quest ->
                    QuestCard(
                        title = quest.title,
                        description = quest.description,
                        xpReward = quest.xpReward,
                        isCompleted = quest.isCompleted
                    )
                }
            }

            // My Tasks
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Tasks (${activeTasks.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onNavigateToTaskList) {
                        Text("View All")
                    }
                }
            }

            items(activeTasks.take(5)) { task ->
                TaskCard(
                    title = task.title,
                    dueDate = task.dueDate,
                    priority = task.priority,
                    onCheck = { viewModel.completeTask(task) }
                )
            }

            // Today's Progress
            item {
                val completedToday = activeTasks.count { it.isCompleted }
                val totalToday = activeTasks.size
                val percentage = if (totalToday > 0) {
                    (completedToday.toFloat() / totalToday * 100).toInt()
                } else {
                    0
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Today's Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = percentage / 100f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$completedToday/$totalToday tasks ($percentage%)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileCard(
    level: Int,
    xp: Int,
    currentStreak: Int,
    totalTasksCompleted: Int
) {
    val requiredXp = 100 + (level - 1) * 50
    val progress = xp.toFloat() / requiredXp

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Level $level",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "XP: $xp/$requiredXp",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$currentStreak days",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Streak",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Text(
                        text = "$totalTasksCompleted tasks completed",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )
        }
    }
}

@Composable
fun QuestCard(
    title: String,
    description: String,
    xpReward: Int,
    isCompleted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reward: $xpReward XP",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (isCompleted) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun TaskCard(
    title: String,
    dueDate: Long?,
    priority: Priority,
    onCheck: () -> Unit
) {
    val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = false,
                onCheckedChange = { onCheck() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (dueDate != null) {
                    Text(
                        text = "Due: ${dateFormat.format(Date(dueDate))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            PriorityBadge(priority)
        }
    }
}

@Composable
fun PriorityBadge(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> MaterialTheme.colorScheme.tertiary
        Priority.MEDIUM -> MaterialTheme.colorScheme.secondary
        Priority.HIGH -> MaterialTheme.colorScheme.primary
        Priority.URGENT -> MaterialTheme.colorScheme.error
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            text = priority.name,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
