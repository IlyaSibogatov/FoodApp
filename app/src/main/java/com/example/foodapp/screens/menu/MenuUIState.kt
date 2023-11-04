package com.example.foodapp.screens.menu

import com.example.foodapp.utils.Constants.BANNER_LIST
import com.example.foodapp.utils.Constants.CITIES_LIST
import com.example.foodapp.utils.InternetConnection
import com.example.foodapp.utils.PageState
import kotlin.random.Random

data class MenuUIState(
    val banners: List<Int> = BANNER_LIST,
    val categories: List<Category> = listOf(),
    val recipes: List<Recipe> = listOf(),

    val selectedCity: String = CITIES_LIST[0],
    val cityList: List<String> = CITIES_LIST,

    val pageState: PageState = PageState.LOAD,
    val internetState: InternetConnection = InternetConnection.NOT_CONNECTED,
)

data class Category(
    val name: String,
    var selected: Boolean = false
)

data class Recipe(
    val image: String,
    val name: String,
    val category: String,
    val description: String,
    val price: String = Random.nextInt(10, 50).toString()
)