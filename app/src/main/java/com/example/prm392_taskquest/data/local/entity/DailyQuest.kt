package com.example.prm392_taskquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quests")
data class DailyQuest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val xpReward: Int,
    val date: Long,
    val isCompleted: Boolean = false,
    val questType: QuestType
)

enum class QuestType {
    DAILY_CHALLENGE,
    QUICK_QUEST,
    BOSS_BATTLE,
    SPEED_RUN,
    PERFECT_DAY
}
