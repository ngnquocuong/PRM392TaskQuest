package com.example.prm392_taskquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val dueDate: Long? = null,
    val priority: Priority,
    val categoryId: Int,
    val projectId: Int? = null,
    val estimatedMinutes: Int = 30,
    val xpReward: Int = 10,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val isRecurring: Boolean = false,
    val recurringType: RecurringType? = null,
    val createdDate: Long = System.currentTimeMillis(),
    val sketchPath: String? = null
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
}

enum class RecurringType {
    DAILY,
    WEEKLY,
    MONTHLY
}
