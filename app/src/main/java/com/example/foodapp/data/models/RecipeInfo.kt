package com.example.foodapp.data.models

import com.google.gson.annotations.SerializedName

data class RecipeInfo(
    @SerializedName("meals") val meals: List<Recipe>
)

data class Recipe(
    @SerializedName("strMealThumb") val image: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strCategory") val category: String,
    @SerializedName("strInstructions") val description: String,
)
