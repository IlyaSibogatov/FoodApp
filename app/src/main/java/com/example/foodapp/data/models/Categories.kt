package com.example.foodapp.data.models

import com.google.gson.annotations.SerializedName

data class Categories(
    val categories: List<Category>
)

data class Category(
    @SerializedName("idCategory") val id: String,
    @SerializedName("strCategory") val name: String,
)