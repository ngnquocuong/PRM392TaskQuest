package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Task
import com.example.prm392_taskquest.data.local.entity.UserProfile
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import java.util.*

data class ProductivityStats(
    val score: Int,
    val completedToday: Int,
    val completedThisWeek: Int,
    val completedThisMonth: Int,
    val onTimePercentage: Int,
    val averageXpPerTask: Int
)

data class DailyTaskCount(
    val date: String,
    val count: Int
)

class StatisticsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    val userProfile: StateFlow<UserProfile?> = repository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val activeTasks: StateFlow<List<Task>> = repository.getActiveTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks: StateFlow<List<Task>> = repository.getCompletedTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val productivityStats: StateFlow<ProductivityStats> = combine(
        activeTasks,
        completedTasks,
        userProfile
    ) { active, completed, profile ->
        calculateProductivityStats(active, completed, profile)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductivityStats(0, 0, 0, 0, 0, 0))

    val last7DaysData: StateFlow<List<DailyTaskCount>> = completedTasks.map { tasks ->
        getLast7DaysData(tasks)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun calculateProductivityStats(
        activeTasks: List<Task>,
        completedTasks: List<Task>,
        userProfile: UserProfile?
    ): ProductivityStats {
        val now = System.currentTimeMillis()
        val todayStart = getTodayStart()
        val weekStart = getWeekStart()
        val monthStart = getMonthStart()

        val completedToday = completedTasks.count {
            it.completedDate != null && it.completedDate >= todayStart
        }
        val completedThisWeek = completedTasks.count {
            it.completedDate != null && it.completedDate >= weekStart
        }
        val completedThisMonth = completedTasks.count {
            it.completedDate != null && it.completedDate >= monthStart
        }

        val totalTasks = activeTasks.size + completedTasks.size
        val completionRate = if (totalTasks > 0) {
            (completedTasks.size.toFloat() / totalTasks * 50).toInt()
        } else {
            0
        }

        val onTimeCount = completedTasks.count { task ->
            task.dueDate != null && task.completedDate != null &&
                    task.completedDate <= task.dueDate + (24 * 60 * 60 * 1000)
        }
        val onTimePercentage = if (completedTasks.isNotEmpty()) {
            (onTimeCount.toFloat() / completedTasks.size * 100).toInt()
        } else {
            0
        }

        val streakScore = if (userProfile != null) {
            (userProfile.currentStreak.toFloat() / 30 * 20).toInt().coerceAtMost(20)
        } else {
            0
        }

        val productivityScore = (completionRate + (onTimePercentage * 0.3).toInt() + streakScore).coerceIn(0, 100)

        val averageXp = if (completedTasks.isNotEmpty()) {
            completedTasks.sumOf { it.xpReward } / completedTasks.size
        } else {
            0
        }

        return ProductivityStats(
            score = productivityScore,
            completedToday = completedToday,
            completedThisWeek = completedThisWeek,
            completedThisMonth = completedThisMonth,
            onTimePercentage = onTimePercentage,
            averageXpPerTask = averageXp
        )
    }

    private fun getLast7DaysData(tasks: List<Task>): List<DailyTaskCount> {
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        val data = mutableListOf<DailyTaskCount>()

        for (i in 6 downTo 0) {
            calendar.timeInMillis = today
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val dayStart = calendar.timeInMillis

            calendar.add(Calendar.DAY_OF_YEAR, 1)
            val dayEnd = calendar.timeInMillis

            val count = tasks.count {
                it.completedDate != null &&
                        it.completedDate >= dayStart &&
                        it.completedDate < dayEnd
            }

            val dateFormat = java.text.SimpleDateFormat("EEE", Locale.getDefault())
            data.add(DailyTaskCount(dateFormat.format(Date(dayStart)), count))
        }

        return data
    }

    private fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getWeekStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun getMonthStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
