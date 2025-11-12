package com.example.prm392_taskquest.data.repository

import com.example.prm392_taskquest.data.local.dao.*
import com.example.prm392_taskquest.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val categoryDao: CategoryDao,
    private val projectDao: ProjectDao,
    private val userProfileDao: UserProfileDao,
    private val achievementDao: AchievementDao,
    private val dailyQuestDao: DailyQuestDao
) {
    // Task operations
    fun getActiveTasks(): Flow<List<Task>> = taskDao.getActiveTasks()
    fun getCompletedTasks(): Flow<List<Task>> = taskDao.getCompletedTasks()
    fun getTasksByCategory(categoryId: Int): Flow<List<Task>> = taskDao.getTasksByCategory(categoryId)
    fun getTaskById(taskId: Int): Flow<Task?> = taskDao.getTaskById(taskId)
    fun searchTasks(query: String): Flow<List<Task>> = taskDao.searchTasks(query)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    suspend fun updateTask(task: Task) = taskDao.updateTask(task)
    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    suspend fun getCompletedTasksCount(startDate: Long): Int = taskDao.getCompletedTasksCount(startDate)
    suspend fun getActiveTasksCount(): Int = taskDao.getActiveTasksCount()

    // Category operations
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    fun getCategoryById(categoryId: Int): Flow<Category?> = categoryDao.getCategoryById(categoryId)
    suspend fun insertCategory(category: Category): Long = categoryDao.insertCategory(category)
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)

    // Project operations
    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()
    fun getActiveProjects(): Flow<List<Project>> = projectDao.getActiveProjects()
    fun getProjectById(projectId: Int): Flow<Project?> = projectDao.getProjectById(projectId)
    suspend fun insertProject(project: Project): Long = projectDao.insertProject(project)
    suspend fun updateProject(project: Project) = projectDao.updateProject(project)

    // User Profile operations
    fun getUserProfile(): Flow<UserProfile?> = userProfileDao.getUserProfile()
    suspend fun getUserProfileSync(): UserProfile? = userProfileDao.getUserProfileSync()
    suspend fun insertUserProfile(userProfile: UserProfile) = userProfileDao.insertUserProfile(userProfile)
    suspend fun updateUserProfile(userProfile: UserProfile) = userProfileDao.updateUserProfile(userProfile)
    suspend fun addXp(xpAmount: Int) = userProfileDao.addXp(xpAmount)
    suspend fun incrementTasksCompleted() = userProfileDao.incrementTasksCompleted()
    suspend fun updateStreak(streak: Int, longestStreak: Int) = userProfileDao.updateStreak(streak, longestStreak)
    suspend fun updateLastActiveDate(date: Long) = userProfileDao.updateLastActiveDate(date)

    // Achievement operations
    fun getAllAchievements(): Flow<List<Achievement>> = achievementDao.getAllAchievements()
    fun getUnlockedAchievements(): Flow<List<Achievement>> = achievementDao.getUnlockedAchievements()
    suspend fun insertAchievement(achievement: Achievement): Long = achievementDao.insertAchievement(achievement)
    suspend fun updateAchievement(achievement: Achievement) = achievementDao.updateAchievement(achievement)
    suspend fun unlockAchievement(achievementId: Int, date: Long) = achievementDao.unlockAchievement(achievementId, date)

    // Daily Quest operations
    fun getQuestsForDate(date: Long): Flow<List<DailyQuest>> = dailyQuestDao.getQuestsForDate(date)
    fun getActiveQuestsForDate(date: Long): Flow<List<DailyQuest>> = dailyQuestDao.getActiveQuestsForDate(date)
    suspend fun insertQuests(quests: List<DailyQuest>) = dailyQuestDao.insertQuests(quests)
    suspend fun completeQuest(questId: Int) = dailyQuestDao.completeQuest(questId)
    suspend fun deleteOldQuests(date: Long) = dailyQuestDao.deleteOldQuests(date)
}
