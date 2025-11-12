package com.example.prm392_taskquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.prm392_taskquest.data.local.database.TaskQuestDatabase
import com.example.prm392_taskquest.data.local.entity.Category
import com.example.prm392_taskquest.ui.navigation.NavGraph
import com.example.prm392_taskquest.ui.theme.PRM392TaskQuestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize database with default categories
        lifecycleScope.launch {
            initializeDatabase()
        }

        setContent {
            PRM392TaskQuestTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }

    private suspend fun initializeDatabase() {
        val database = TaskQuestDatabase.getDatabase(applicationContext)
        val categoryDao = database.categoryDao()

        // Check if categories already exist
        val existingCategories = categoryDao.getAllCategories()

        // Insert default categories if none exist
        val defaultCategories = listOf(
            Category(id = 1, name = "Work", color = "#3B82F6", icon = "work"),
            Category(id = 2, name = "Study", color = "#8B5CF6", icon = "school"),
            Category(id = 3, name = "Personal", color = "#10B981", icon = "person"),
            Category(id = 4, name = "Health", color = "#EF4444", icon = "favorite"),
            Category(id = 5, name = "Finance", color = "#F59E0B", icon = "payments"),
            Category(id = 6, name = "Creative", color = "#EC4899", icon = "palette")
        )

        defaultCategories.forEach { category ->
            try {
                categoryDao.insertCategory(category)
            } catch (e: Exception) {
                // Category already exists, ignore
            }
        }
    }
}