package com.example.prm392_taskquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val icon: String,
    val isUnlocked: Boolean = false,
    val unlockedDate: Long? = null,
    val requiredCount: Int,
    val currentCount: Int = 0,
    val type: AchievementType
)

enum class AchievementType {
    TASKS_COMPLETED,
    STREAK,
    LEVEL,
    CATEGORY,
    PERFECT_WEEK
}
