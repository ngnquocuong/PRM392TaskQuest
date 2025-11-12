package com.example.prm392_taskquest.ui.screens.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prm392_taskquest.ui.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val productivityStats by viewModel.productivityStats.collectAsState()
    val last7DaysData by viewModel.last7DaysData.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Productivity Score
            item {
                Text(
                    text = "Productivity Score",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProductivityGauge(score = productivityStats.score)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${productivityStats.score}%",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = getProductivityLabel(productivityStats.score),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            // Quick Stats
            item {
                Text(
                    text = "Overview",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "Today",
                        value = productivityStats.completedToday.toString(),
                        icon = Icons.Default.CheckCircle,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "This Week",
                        value = productivityStats.completedThisWeek.toString(),
                        icon = Icons.Default.DateRange,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "This Month",
                        value = productivityStats.completedThisMonth.toString(),
                        icon = Icons.Default.Home,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "On-Time",
                        value = "${productivityStats.onTimePercentage}%",
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Last 7 Days Chart
            item {
                Text(
                    text = "Last 7 Days",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        SimpleBarChart(
                            data = last7DaysData.map { it.count },
                            labels = last7DaysData.map { it.date },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                }
            }

            // User Stats
            userProfile?.let { profile ->
                item {
                    Text(
                        text = "Your Progress",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatRow(
                                label = "Total Tasks Completed",
                                value = profile.totalTasksCompleted.toString()
                            )
                            StatRow(
                                label = "Current Level",
                                value = profile.level.toString()
                            )
                            StatRow(
                                label = "Total XP Earned",
                                value = profile.xp.toString()
                            )
                            StatRow(
                                label = "Average XP per Task",
                                value = productivityStats.averageXpPerTask.toString()
                            )
                            StatRow(
                                label = "Current Streak",
                                value = "${profile.currentStreak} days"
                            )
                            StatRow(
                                label = "Longest Streak",
                                value = "${profile.longestStreak} days"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductivityGauge(
    score: Int,
    modifier: Modifier = Modifier
) {
    val sweepAngle = (score / 100f) * 270f

    Canvas(modifier = modifier.size(120.dp)) {
        val strokeWidth = 20.dp.toPx()

        // Background arc
        drawArc(
            color = Color.LightGray.copy(alpha = 0.3f),
            startAngle = 135f,
            sweepAngle = 270f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )

        // Progress arc
        val color = when {
            score >= 80 -> Color(0xFF10B981) // Green
            score >= 50 -> Color(0xFFF59E0B) // Amber
            else -> Color(0xFFEF4444) // Red
        }

        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            size = Size(size.width, size.height)
        )
    }
}

@Composable
fun SimpleBarChart(
    data: List<Int>,
    labels: List<String>,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOrNull() ?: 1

    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 2)
        val spacing = barWidth

        data.forEachIndexed { index, value ->
            val barHeight = if (maxValue > 0) {
                (value.toFloat() / maxValue) * (size.height - 40.dp.toPx())
            } else {
                0f
            }

            val x = index * (barWidth + spacing) + spacing
            val y = size.height - barHeight - 20.dp.toPx()

            // Draw bar
            drawRect(
                color = Color(0xFF6366F1),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }

    // Labels
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        labels.forEach { label ->
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun getProductivityLabel(score: Int): String {
    return when {
        score >= 80 -> "Excellent!"
        score >= 60 -> "Great!"
        score >= 40 -> "Good"
        score >= 20 -> "Fair"
        else -> "Keep Going!"
    }
}
