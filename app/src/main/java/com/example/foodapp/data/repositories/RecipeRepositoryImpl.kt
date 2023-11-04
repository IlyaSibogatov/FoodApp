package com.example.foodapp.data.repositories

import com.example.foodapp.data.db.RecipeDao
import com.example.foodapp.data.db.RecipeEntity
import com.example.foodapp.data.services.RecipesService
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipesService: RecipesService,
    private val recipeDao: RecipeDao
) : RecipesRepository {

    override suspend fun getRecipesByCategory(category: String) =
        recipesService.getRecipesByCategory(category)

    override suspend fun getRecipeById(id: String) = recipesService.getRecipeById(id)

    override suspend fun insertRecipe(recipe: RecipeEntity) = recipeDao.insertRecipe(recipe)

    override suspend fun getRecipesDao(category: String)  = recipeDao.getRecipes(category)

    override suspend fun cleanCategoryRecipes(category: String) = recipeDao.cleanCategoryRecipes(category)
}