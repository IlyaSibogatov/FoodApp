package com.example.foodapp.data.models

import com.google.gson.annotations.SerializedName

data class Recipes(
    @SerializedName("meals") val ids: List<RecipeId>
)

data class RecipeId(
    @SerializedName("idMeal") val id: String,
)
