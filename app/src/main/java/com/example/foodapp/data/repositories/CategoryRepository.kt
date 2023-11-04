package com.example.foodapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.foodapp.data.db.CategoryEntity
import com.example.foodapp.data.models.Categories

interface CategoryRepository {

    suspend fun getCategories(): Categories

    suspend fun insertCategory(category: CategoryEntity)

    suspend fun getCategoriesFromDao(): List<CategoryEntity>

    suspend fun cleanCategories()
}