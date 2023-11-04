package com.example.foodapp.data.repositories

import androidx.lifecycle.LiveData
import com.example.foodapp.data.db.RecipeEntity
import com.example.foodapp.data.models.RecipeInfo
import com.example.foodapp.data.models.Recipes

interface RecipesRepository {

    suspend fun getRecipesByCategory(category: String): Recipes

    suspend fun getRecipeById(id: String): RecipeInfo

    suspend fun insertRecipe(recipe: RecipeEntity)

    suspend fun getRecipesDao(category: String): List<RecipeEntity>

    suspend fun cleanCategoryRecipes(category: String)
}