package com.example.foodapp.data.services

import com.example.foodapp.data.models.Categories
import com.example.foodapp.data.models.RecipeInfo
import com.example.foodapp.data.models.Recipes
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesService {

    @GET("categories.php")
    suspend fun getCategories(): Categories

    @GET("filter.php")
    suspend fun getRecipesByCategory(
        @Query("c") category: String
    ): Recipes

    @GET("lookup.php")
    suspend fun getRecipeById(
        @Query("i") id: String
    ): RecipeInfo
}