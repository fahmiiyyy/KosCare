package com.example.koscare.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.ShoppingCart

sealed class BottomNavItem(

    val route: String,
    val title: String,
    val icon: ImageVector
) {

    data object Home : BottomNavItem(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    data object Expense : BottomNavItem(
        route = "expense",
        title = "Expense",
        icon = Icons.Default.Receipt
    )

    data object Schedule : BottomNavItem(
        route = "schedule",
        title = "Schedule",
        icon = Icons.Default.Schedule
    )

    data object Profile : BottomNavItem(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Shopping : BottomNavItem(
        route = "shopping",
        title = "Shopping",
        icon = Icons.Default.ShoppingCart
    )
}