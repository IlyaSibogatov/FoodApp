package com.example.foodapp.utils

import com.example.foodapp.R

object Constants {
    const val MENU_ROUTE = "menu_route"
    const val CART_ROUTE = "cart_route"
    const val PROFILE_ROUTE = "profile_route"

    const val CONTENT_DESCRIPTION = "description"

    const val BASE_URL = "https://themealdb.com/api/json/v1/1/"
    const val PREVIEW_PATH = "/preview"

    val CITIES_LIST = listOf("London", "Paris", "Berlin")
    val BANNER_LIST = listOf(
        R.drawable.steak_banner,
        R.drawable.soda_banner,
        R.drawable.pizza_banner,
        R.drawable.coffee_banner,
    )
}