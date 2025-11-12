package com.example.prm392_taskquest.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Priority
import com.example.prm392_taskquest.data.local.entity.RecurringType
import com.example.prm392_taskquest.data.local.entity.Task
import com.example.prm392_taskquest.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    private val database = TaskQuestDatabase.getDatabase(application)
    private val repository = TaskRepository(
        database.taskDao(),
        database.categoryDao(),
        database.projectDao(),
        database.userProfileDao(),
        database.achievementDao(),
        database.dailyQuestDao()
    )

    private val _taskTitle = MutableStateFlow("")
    val taskTitle: StateFlow<String> = _taskTitle.asStateFlow()

    private val _taskDescription = MutableStateFlow("")
    val taskDescription: StateFlow<String> = _taskDescription.asStateFlow()

    private val _taskPriority = MutableStateFlow(Priority.MEDIUM)
    val taskPriority: StateFlow<Priority> = _taskPriority.asStateFlow()

    private val _taskCategoryId = MutableStateFlow(1)
    val taskCategoryId: StateFlow<Int> = _taskCategoryId.asStateFlow()

    private val _taskDueDate = MutableStateFlow<Long?>(null)
    val taskDueDate: StateFlow<Long?> = _taskDueDate.asStateFlow()

    private val _taskEstimatedMinutes = MutableStateFlow(30)
    val taskEstimatedMinutes: StateFlow<Int> = _taskEstimatedMinutes.asStateFlow()

    private val _taskXpReward = MutableStateFlow(10)
    val taskXpReward: StateFlow<Int> = _taskXpReward.asStateFlow()

    private val _isRecurring = MutableStateFlow(false)
    val isRecurring: StateFlow<Boolean> = _isRecurring.asStateFlow()

    private val _recurringType = MutableStateFlow<RecurringType?>(null)
    val recurringType: StateFlow<RecurringType?> = _recurringType.asStateFlow()

    private val taskId: Int = savedStateHandle.get<Int>("taskId") ?: -1
    private var isEditMode = taskId != -1

    init {
        if (isEditMode) {
            loadTask(taskId)
        }
    }

    private fun loadTask(taskId: Int) {
        viewModelScope.launch {
            repository.getTaskById(taskId).collect { task ->
                task?.let {
                    _taskTitle.value = it.title
                    _taskDescription.value = it.description
                    _taskPriority.value = it.priority
                    _taskCategoryId.value = it.categoryId
                    _taskDueDate.value = it.dueDate
                    _taskEstimatedMinutes.value = it.estimatedMinutes
                    _taskXpReward.value = it.xpReward
                    _isRecurring.value = it.isRecurring
                    _recurringType.value = it.recurringType
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _taskTitle.value = title
    }

    fun updateDescription(description: String) {
        _taskDescription.value = description
    }

    fun updatePriority(priority: Priority) {
        _taskPriority.value = priority
        updateXpReward()
    }

    fun updateCategoryId(categoryId: Int) {
        _taskCategoryId.value = categoryId
    }

    fun updateDueDate(dueDate: Long?) {
        _taskDueDate.value = dueDate
    }

    fun updateEstimatedMinutes(minutes: Int) {
        _taskEstimatedMinutes.value = minutes
    }

    fun updateXpReward(xp: Int? = null) {
        if (xp != null) {
            _taskXpReward.value = xp
        } else {
            // Auto-calculate based on priority and estimated time
            val baseXp = when (_taskPriority.value) {
                Priority.LOW -> 10
                Priority.MEDIUM -> 15
                Priority.HIGH -> 25
                Priority.URGENT -> 40
            }
            val timeBonus = (_taskEstimatedMinutes.value / 30) * 5
            _taskXpReward.value = baseXp + timeBonus
        }
    }

    fun updateRecurring(isRecurring: Boolean) {
        _isRecurring.value = isRecurring
    }

    fun updateRecurringType(type: RecurringType?) {
        _recurringType.value = type
    }

    fun saveTask(onSuccess: () -> Unit) {
        if (_taskTitle.value.isBlank()) {
            return
        }

        viewModelScope.launch {
            val task = Task(
                id = if (isEditMode) taskId else 0,
                title = _taskTitle.value,
                description = _taskDescription.value,
                priority = _taskPriority.value,
                categoryId = _taskCategoryId.value,
                dueDate = _taskDueDate.value,
                estimatedMinutes = _taskEstimatedMinutes.value,
                xpReward = _taskXpReward.value,
                isRecurring = _isRecurring.value,
                recurringType = _recurringType.value
            )

            if (isEditMode) {
                repository.updateTask(task)
            } else {
                repository.insertTask(task)
            }

            onSuccess()
        }
    }
}
