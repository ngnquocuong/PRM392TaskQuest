package com.example.prm392_taskquest.data.local.dao

import androidx.room.*
import com.example.prm392_taskquest.data.local.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryById(categoryId: Int): Flow<Category?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("UPDATE categories SET taskCount = taskCount + 1 WHERE id = :categoryId")
    suspend fun incrementTaskCount(categoryId: Int)

    @Query("UPDATE categories SET taskCount = taskCount - 1 WHERE id = :categoryId AND taskCount > 0")
    suspend fun decrementTaskCount(categoryId: Int)
}
