package com.example.foodapp.data.repositories

import com.example.foodapp.data.db.CategoryDao
import com.example.foodapp.data.db.CategoryEntity
import com.example.foodapp.data.models.Categories
import com.example.foodapp.data.services.RecipesService
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val recipesService: RecipesService,
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun getCategories(): Categories = recipesService.getCategories()

    override suspend fun insertCategory(category: CategoryEntity) =
        categoryDao.insertCategory(category)

    override suspend fun getCategoriesFromDao() = categoryDao.getCategories()

    override suspend fun cleanCategories() = categoryDao.cleanCategories()
}