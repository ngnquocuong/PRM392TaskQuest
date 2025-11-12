package com.example.prm392_taskquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val level: Int = 1,
    val xp: Int = 0,
    val totalTasksCompleted: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastActiveDate: Long = System.currentTimeMillis(),
    val characterClass: CharacterClass = CharacterClass.WARRIOR,
    val avatarId: Int = 1
)

enum class CharacterClass {
    WARRIOR,
    MAGE,
    ROGUE,
    PALADIN
}
