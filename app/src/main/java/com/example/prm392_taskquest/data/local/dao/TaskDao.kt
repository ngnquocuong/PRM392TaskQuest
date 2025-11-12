package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY dueDate ASC")
    fun getActiveTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedDate DESC")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId")
    fun getTasksByCategory(categoryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    fun getTasksByProject(projectId: Int): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Int): Flow<Task?>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task): Long

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 1 AND completedDate >= :startDate")
    suspend fun getCompletedTasksCount(startDate: Long): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE isCompleted = 0")
    suspend fun getActiveTasksCount(): Int

    @Query("SELECT * FROM tasks WHERE dueDate >= :startDate AND dueDate <= :endDate")
    fun getTasksInDateRange(startDate: Long, endDate: Long): Flow<List<Task>>
}
