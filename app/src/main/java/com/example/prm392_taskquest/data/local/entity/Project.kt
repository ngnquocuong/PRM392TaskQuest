package com.example.prm392_taskquest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val color: String
)
