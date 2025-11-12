package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.*
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
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

    val todayQuests: StateFlow<List<DailyQuest>> = repository.getQuestsForDate(getTodayStart())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        initializeUserProfile()
        generateDailyQuests()
        updateStreak()
    }

    private fun initializeUserProfile() {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync()
            if (profile == null) {
                repository.insertUserProfile(UserProfile())
            }
        }
    }

    private fun generateDailyQuests() {
        viewModelScope.launch {
            val quests = repository.getQuestsForDate(getTodayStart()).first()
            if (quests.isEmpty()) {
                // Generate new daily quests
                val newQuests = listOf(
                    DailyQuest(
                        title = "Quick Quest",
                        description = "Complete 3 tasks today",
                        xpReward = 50,
                        date = getTodayStart(),
                        questType = QuestType.QUICK_QUEST
                    ),
                    DailyQuest(
                        title = "Perfect Day",
                        description = "Complete all scheduled tasks for today",
                        xpReward = 100,
                        date = getTodayStart(),
                        questType = QuestType.PERFECT_DAY
                    )
                )
                repository.insertQuests(newQuests)

                // Clean up old quests (older than 7 days)
                repository.deleteOldQuests(getTodayStart() - 7 * 24 * 60 * 60 * 1000)
            }
        }
    }

    private fun updateStreak() {
        viewModelScope.launch {
            val profile = repository.getUserProfileSync() ?: return@launch
            val lastActiveDate = profile.lastActiveDate
            val currentDate = System.currentTimeMillis()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = lastActiveDate
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val lastActiveDay = calendar.timeInMillis

            calendar.timeInMillis = currentDate
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val currentDay = calendar.timeInMillis

            val daysDiff = ((currentDay - lastActiveDay) / (24 * 60 * 60 * 1000)).toInt()

            when {
                daysDiff == 0 -> {
                    // Same day, do nothing
                }
                daysDiff == 1 -> {
                    // Consecutive day, increment streak
                    val newStreak = profile.currentStreak + 1
                    val longestStreak = maxOf(newStreak, profile.longestStreak)
                    repository.updateStreak(newStreak, longestStreak)
                    repository.updateLastActiveDate(currentDate)
                }
                else -> {
                    // Streak broken, reset to 1
                    repository.updateStreak(1, profile.longestStreak)
                    repository.updateLastActiveDate(currentDate)
                }
            }
        }
    }

    fun completeTask(task: Task) {
        viewModelScope.launch {
            val completedTask = task.copy(
                isCompleted = true,
                completedDate = System.currentTimeMillis()
            )
            repository.updateTask(completedTask)

            // Calculate and award XP
            val xp = calculateXp(task)
            repository.addXp(xp)
            repository.incrementTasksCompleted()

            // Check for level up
            checkLevelUp()

            // Check quest progress
            checkQuestProgress()
        }
    }

    private fun calculateXp(task: Task): Int {
        var baseXp = task.xpReward

        // Priority multiplier
        val priorityMultiplier = when (task.priority) {
            Priority.LOW -> 1.0
            Priority.MEDIUM -> 1.5
            Priority.HIGH -> 2.0
            Priority.URGENT -> 3.0
        }

        // Time bonus
        val timeMultiplier = if (task.dueDate != null) {
            val completedDate = task.completedDate ?: System.currentTimeMillis()
            when {
                completedDate < task.dueDate -> 1.2 // Early
                completedDate <= task.dueDate + (24 * 60 * 60 * 1000) -> 1.0 // On time
                else -> 0.9 // Late
            }
        } else {
            1.0
        }

        // Streak bonus (max 50%)
        val profile = userProfile.value
        val streakMultiplier = if (profile != null) {
            1.0 + minOf(profile.currentStreak * 0.05, 0.5)
        } else {
            1.0
        }

        return (baseXp * priorityMultiplier * timeMultiplier * streakMultiplier).toInt()
    }

    private suspend fun checkLevelUp() {
        val profile = repository.getUserProfileSync() ?: return
        val requiredXp = calculateRequiredXp(profile.level)

        if (profile.xp >= requiredXp) {
            val newLevel = profile.level + 1
            val newXp = profile.xp - requiredXp
            repository.updateUserProfile(
                profile.copy(
                    level = newLevel,
                    xp = newXp
                )
            )
        }
    }

    private fun calculateRequiredXp(level: Int): Int {
        return 100 + (level - 1) * 50
    }

    private suspend fun checkQuestProgress() {
        val quests = repository.getActiveQuestsForDate(getTodayStart()).first()
        val completedCount = repository.getCompletedTasksCount(getTodayStart())

        quests.forEach { quest ->
            when (quest.questType) {
                QuestType.QUICK_QUEST -> {
                    if (completedCount >= 3 && !quest.isCompleted) {
                        repository.completeQuest(quest.id)
                        repository.addXp(quest.xpReward)
                    }
                }
                else -> {
                    // Handle other quest types
                }
            }
        }
    }

    private fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
