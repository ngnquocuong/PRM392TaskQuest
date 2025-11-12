package com.example.prm392_taskquest.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object TaskList : Screen("task_list")
    object AddEditTask : Screen("add_edit_task/{taskId}") {
        fun createRoute(taskId: Int? = null) = "add_edit_task/${taskId ?: -1}"
    }
    object Statistics : Screen("statistics")
    object Profile : Screen("profile")
    object TaskCompletion : Screen("task_completion/{taskId}") {
        fun createRoute(taskId: Int) = "task_completion/$taskId"
    }
    object Drawing : Screen("drawing/{taskId}") {
        fun createRoute(taskId: Int) = "drawing/$taskId"
    }
    object CategoryList : Screen("category_list")
    object ProjectList : Screen("project_list")
    object Achievements : Screen("achievements")
}
