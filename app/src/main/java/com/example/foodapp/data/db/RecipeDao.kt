package com.example.foodapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipe(recipe: RecipeEntity)

    @Query("SELECT * FROM recipe_table WHERE category = :category")
    fun getRecipes(category: String): List<RecipeEntity>

    @Query("DELETE FROM recipe_table WHERE category = :category")
    fun cleanCategoryRecipes(category: String)
}