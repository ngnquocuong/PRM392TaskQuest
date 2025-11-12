package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Project
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProjectViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    val projects: StateFlow<List<Project>> = repository.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProject(name: String, description: String, deadline: Long?, color: String) {
        viewModelScope.launch {
            val project = Project(
                name = name,
                description = description,
                deadline = deadline,
                color = color
            )
            repository.insertProject(project)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            repository.updateProject(project)
        }
    }

    fun toggleProjectCompletion(project: Project) {
        viewModelScope.launch {
            val updated = project.copy(isCompleted = !project.isCompleted)
            repository.updateProject(updated)
        }
    }
}
