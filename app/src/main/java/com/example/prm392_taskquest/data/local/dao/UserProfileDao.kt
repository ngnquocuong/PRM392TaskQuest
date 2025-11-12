package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Update
    suspend fun updateUserProfile(userProfile: UserProfile)

    @Query("UPDATE user_profile SET xp = xp + :xpAmount WHERE id = 1")
    suspend fun addXp(xpAmount: Int)

    @Query("UPDATE user_profile SET level = :newLevel WHERE id = 1")
    suspend fun updateLevel(newLevel: Int)

    @Query("UPDATE user_profile SET totalTasksCompleted = totalTasksCompleted + 1 WHERE id = 1")
    suspend fun incrementTasksCompleted()

    @Query("UPDATE user_profile SET currentStreak = :streak, longestStreak = :longestStreak WHERE id = 1")
    suspend fun updateStreak(streak: Int, longestStreak: Int)

    @Query("UPDATE user_profile SET lastActiveDate = :date WHERE id = 1")
    suspend fun updateLastActiveDate(date: Long)
}
