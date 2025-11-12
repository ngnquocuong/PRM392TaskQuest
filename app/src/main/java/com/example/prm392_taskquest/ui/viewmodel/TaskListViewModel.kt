package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Priority
import com.example.prm392_taskquest.data.local.entity.Task
import com.example.prm392_taskquest.data.repository.TaskRepository
import com.example.prm392_taskquest.ui.components.SortOption
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showCompleted = MutableStateFlow(false)
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    private val _selectedPriority = MutableStateFlow<Priority?>(null)
    val selectedPriority: StateFlow<Priority?> = _selectedPriority.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Int?>(null)
    val selectedCategory: StateFlow<Int?> = _selectedCategory.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.DUE_DATE_ASC)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    val tasks: StateFlow<List<Task>> = _showCompleted.flatMapLatest { showCompleted ->
        if (showCompleted) {
            repository.getCompletedTasks()
        } else {
            repository.getActiveTasks()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredTasks: StateFlow<List<Task>> = combine(
        tasks,
        _searchQuery,
        _selectedPriority,
        _selectedCategory,
        _sortOption
    ) { taskList, query, priority, category, sort ->
        var filtered = taskList

        // Apply search filter
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        }

        // Apply priority filter
        if (priority != null) {
            filtered = filtered.filter { it.priority == priority }
        }

        // Apply category filter
        if (category != null) {
            filtered = filtered.filter { it.categoryId == category }
        }

        // Apply sorting
        filtered = when (sort) {
            SortOption.DUE_DATE_ASC -> filtered.sortedBy { it.dueDate ?: Long.MAX_VALUE }
            SortOption.DUE_DATE_DESC -> filtered.sortedByDescending { it.dueDate ?: 0L }
            SortOption.PRIORITY_HIGH_TO_LOW -> filtered.sortedByDescending { it.priority.ordinal }
            SortOption.PRIORITY_LOW_TO_HIGH -> filtered.sortedBy { it.priority.ordinal }
            SortOption.XP_HIGH_TO_LOW -> filtered.sortedByDescending { it.xpReward }
            SortOption.XP_LOW_TO_HIGH -> filtered.sortedBy { it.xpReward }
            SortOption.CREATED_DATE_DESC -> filtered.sortedByDescending { it.createdDate }
            SortOption.CREATED_DATE_ASC -> filtered.sortedBy { it.createdDate }
        }

        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleShowCompleted() {
        _showCompleted.value = !_showCompleted.value
    }

    fun updatePriorityFilter(priority: Priority?) {
        _selectedPriority.value = priority
    }

    fun updateCategoryFilter(categoryId: Int?) {
        _selectedCategory.value = categoryId
    }

    fun updateSortOption(option: SortOption) {
        _sortOption.value = option
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(
                isCompleted = !task.isCompleted,
                completedDate = if (!task.isCompleted) System.currentTimeMillis() else null
            )
            repository.updateTask(updatedTask)

            if (!task.isCompleted) {
                // Task was just completed, award XP
                repository.addXp(task.xpReward)
                repository.incrementTasksCompleted()
            }
        }
    }
}
