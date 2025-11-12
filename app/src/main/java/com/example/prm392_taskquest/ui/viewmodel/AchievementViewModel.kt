package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Achievement
import com.example.prm392_taskquest.data.local.entity.AchievementType
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AchievementViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    val achievements: StateFlow<List<Achievement>> = repository.getAllAchievements()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        initializeAchievements()
    }

    private fun initializeAchievements() {
        viewModelScope.launch {
            val existingAchievements = repository.getAllAchievements().first()
            if (existingAchievements.isEmpty()) {
                val defaultAchievements = listOf(
                    Achievement(
                        title = "First Steps",
                        description = "Complete your first task",
                        icon = "star",
                        requiredCount = 1,
                        type = AchievementType.TASKS_COMPLETED
                    ),
                    Achievement(
                        title = "Task Master",
                        description = "Complete 10 tasks",
                        icon = "star",
                        requiredCount = 10,
                        type = AchievementType.TASKS_COMPLETED
                    ),
                    Achievement(
                        title = "Productivity King",
                        description = "Complete 50 tasks",
                        icon = "star",
                        requiredCount = 50,
                        type = AchievementType.TASKS_COMPLETED
                    ),
                    Achievement(
                        title = "Legend",
                        description = "Complete 100 tasks",
                        icon = "star",
                        requiredCount = 100,
                        type = AchievementType.TASKS_COMPLETED
                    ),
                    Achievement(
                        title = "On Fire",
                        description = "Maintain a 7-day streak",
                        icon = "fire",
                        requiredCount = 7,
                        type = AchievementType.STREAK
                    ),
                    Achievement(
                        title = "Unstoppable",
                        description = "Maintain a 30-day streak",
                        icon = "fire",
                        requiredCount = 30,
                        type = AchievementType.STREAK
                    ),
                    Achievement(
                        title = "Dedication",
                        description = "Maintain a 100-day streak",
                        icon = "fire",
                        requiredCount = 100,
                        type = AchievementType.STREAK
                    ),
                    Achievement(
                        title = "Rising Star",
                        description = "Reach Level 5",
                        icon = "level",
                        requiredCount = 5,
                        type = AchievementType.LEVEL
                    ),
                    Achievement(
                        title = "Elite",
                        description = "Reach Level 10",
                        icon = "level",
                        requiredCount = 10,
                        type = AchievementType.LEVEL
                    ),
                    Achievement(
                        title = "Master",
                        description = "Reach Level 20",
                        icon = "level",
                        requiredCount = 20,
                        type = AchievementType.LEVEL
                    ),
                    Achievement(
                        title = "Perfect Week",
                        description = "Complete all tasks for 7 consecutive days",
                        icon = "perfect",
                        requiredCount = 7,
                        type = AchievementType.PERFECT_WEEK
                    )
                )

                defaultAchievements.forEach { achievement ->
                    repository.insertAchievement(achievement)
                }
            }
        }
    }

    suspend fun checkAndUnlockAchievements() {
        val profile = repository.getUserProfileSync() ?: return
        val allAchievements = repository.getAllAchievements().first()

        allAchievements.filter { !it.isUnlocked }.forEach { achievement ->
            val shouldUnlock = when (achievement.type) {
                AchievementType.TASKS_COMPLETED -> {
                    profile.totalTasksCompleted >= achievement.requiredCount
                }
                AchievementType.STREAK -> {
                    profile.currentStreak >= achievement.requiredCount
                }
                AchievementType.LEVEL -> {
                    profile.level >= achievement.requiredCount
                }
                else -> false
            }

            if (shouldUnlock) {
                repository.unlockAchievement(achievement.id, System.currentTimeMillis())
            } else {
                // Update progress
                val currentProgress = when (achievement.type) {
                    AchievementType.TASKS_COMPLETED -> profile.totalTasksCompleted
                    AchievementType.STREAK -> profile.currentStreak
                    AchievementType.LEVEL -> profile.level
                    else -> 0
                }
                repository.updateAchievement(achievement.copy(currentCount = currentProgress))
            }
        }
    }
}
