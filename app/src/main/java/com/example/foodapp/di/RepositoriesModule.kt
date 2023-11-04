package com.example.foodapp.di

import com.example.foodapp.data.repositories.CategoryRepository
import com.example.foodapp.data.repositories.CategoryRepositoryImpl
import com.example.foodapp.data.repositories.RecipeRepositoryImpl
import com.example.foodapp.data.repositories.RecipesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract fun provideRecipeRepository(
        recipeRepo: RecipeRepositoryImpl
    ): RecipesRepository

    @Binds
    @Singleton
    abstract fun provideCategoryRepository(
        categoryRepo: CategoryRepositoryImpl
    ): CategoryRepository
}