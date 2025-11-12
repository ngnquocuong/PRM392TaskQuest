package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.DailyQuest
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyQuestDao {
    @Query("SELECT * FROM daily_quests WHERE date = :date")
    fun getQuestsForDate(date: Long): Flow<List<DailyQuest>>

    @Query("SELECT * FROM daily_quests WHERE date = :date AND isCompleted = 0")
    fun getActiveQuestsForDate(date: Long): Flow<List<DailyQuest>>

    @Query("SELECT * FROM daily_quests WHERE id = :questId")
    fun getQuestById(questId: Int): Flow<DailyQuest?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuest(quest: DailyQuest): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuests(quests: List<DailyQuest>)

    @Update
    suspend fun updateQuest(quest: DailyQuest)

    @Delete
    suspend fun deleteQuest(quest: DailyQuest)

    @Query("DELETE FROM daily_quests WHERE date < :date")
    suspend fun deleteOldQuests(date: Long)

    @Query("UPDATE daily_quests SET isCompleted = 1 WHERE id = :questId")
    suspend fun completeQuest(questId: Int)
}
