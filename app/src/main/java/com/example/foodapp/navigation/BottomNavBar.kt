package com.example.foodapp.navigation

import com.example.foodapp.R
import com.example.foodapp.utils.Constants.CART_ROUTE
import com.example.foodapp.utils.Constants.MENU_ROUTE
import com.example.foodapp.utils.Constants.PROFILE_ROUTE

sealed class BottomNavBar(
    val route: String,
    val title: Int,
    val icon: Int,
) {
    object Menu : BottomNavBar(
        route = MENU_ROUTE,
        title = R.string.menu_label,
        icon = R.drawable.ic_menu_icon
    )

    object Profile : BottomNavBar(
        route = PROFILE_ROUTE,
        title = R.string.profile_label,
        icon = R.drawable.ic_profile
    )

    object Cart : BottomNavBar(
        route = CART_ROUTE,
        title = R.string.cart_label,
        icon = R.drawable.ic_cart
    )
}
