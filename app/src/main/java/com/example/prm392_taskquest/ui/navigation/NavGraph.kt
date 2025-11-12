package com.example.prm392_taskquest.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.prm392_taskquest.ui.screens.home.HomeScreen
import com.example.prm392_taskquest.ui.screens.task.AddEditTaskScreen
import com.example.prm392_taskquest.ui.screens.task.TaskListScreen
import com.example.prm392_taskquest.ui.screens.category.CategoryScreen
import com.example.prm392_taskquest.ui.screens.project.ProjectScreen
import com.example.prm392_taskquest.ui.screens.profile.ProfileScreen
import com.example.prm392_taskquest.ui.screens.achievement.AchievementScreen
import com.example.prm392_taskquest.ui.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddEditTask.createRoute())
                },
                onNavigateToTaskList = {
                    navController.navigate(Screen.TaskList.route)
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onNavigateToAchievements = {
                    navController.navigate(Screen.Achievements.route)
                }
            )
        }

        composable(route = Screen.TaskList.route) {
            TaskListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddTask = {
                    navController.navigate(Screen.AddEditTask.createRoute())
                },
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.AddEditTask.createRoute(taskId))
                }
            )
        }

        composable(
            route = Screen.AddEditTask.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditTaskScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.CategoryList.route) {
            CategoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ProjectList.route) {
            ProjectScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Achievements.route) {
            AchievementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
