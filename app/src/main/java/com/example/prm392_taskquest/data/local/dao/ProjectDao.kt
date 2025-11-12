package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY deadline ASC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE isCompleted = 0 ORDER BY deadline ASC")
    fun getActiveProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getProjectById(projectId: Int): Flow<Project?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)
}
