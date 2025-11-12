package com.example.prm392_taskquest.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.prm392_taskquest.data.local.converters.Converters
import com.example.prm392_taskquest.data.local.dao.*
import com.example.prm392_taskquest.data.local.entity.*

@Database(
    entities = [
        Task::class,
        Category::class,
        Project::class,
        UserProfile::class,
        Achievement::class,
        DailyQuest::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TaskQuestDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun projectDao(): ProjectDao
    abstract fun userProfileDao(): UserProfileDao
    abstract fun achievementDao(): AchievementDao
    abstract fun dailyQuestDao(): DailyQuestDao

    companion object {
        @Volatile
        private var INSTANCE: TaskQuestDatabase? = null

        fun getDatabase(context: Context): TaskQuestDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskQuestDatabase::class.java,
                    "task_quest_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
