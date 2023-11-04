package com.example.foodapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CategoryEntity::class, RecipeEntity::class],
    version = 1,
    exportSchema = false
)

abstract class DataBase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun recipeDao(): RecipeDao
}