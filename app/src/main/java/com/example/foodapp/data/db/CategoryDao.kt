package com.example.foodapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM category_table")
    fun getCategories(): List<CategoryEntity>

    @Query("DELETE FROM category_table")
    fun cleanCategories()
}