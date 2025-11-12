package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.Achievement
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, id ASC")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedDate DESC")
    fun getUnlockedAchievements(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE isUnlocked = 0")
    fun getLockedAchievements(): Flow<List<Achievement>>

    @Query("SELECT * FROM achievements WHERE id = :achievementId")
    fun getAchievementById(achievementId: Int): Flow<Achievement?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: Achievement): Long

    @Update
    suspend fun updateAchievement(achievement: Achievement)

    @Delete
    suspend fun deleteAchievement(achievement: Achievement)

    @Query("UPDATE achievements SET currentCount = :count WHERE id = :achievementId")
    suspend fun updateProgress(achievementId: Int, count: Int)

    @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :date WHERE id = :achievementId")
    suspend fun unlockAchievement(achievementId: Int, date: Long)
}
