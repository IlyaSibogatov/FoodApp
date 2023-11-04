package com.example.foodapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodapp.screens.menu.MenuScreen
import com.example.foodapp.screens.others.OtherScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavBar.Menu.route
    ) {
        composable(route = BottomNavBar.Menu.route) {
            MenuScreen()
        }
        composable(route = BottomNavBar.Cart.route) {
            OtherScreen()
        }
        composable(route = BottomNavBar.Profile.route) {
            OtherScreen()
        }
    }
}