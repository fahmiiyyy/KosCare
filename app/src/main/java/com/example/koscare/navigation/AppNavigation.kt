package com.example.koscare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koscare.ui.screen.DashboardScreen
import com.example.koscare.ui.screen.LoginScreen
import com.example.koscare.viewmodel.AuthViewModel
import com.example.koscare.ui.screen.ShoppingScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val authViewModel = remember {
        AuthViewModel()
    }

    val startDestination = if (
        authViewModel.isUserLoggedIn()
    ) {
        "dashboard"
    } else {
        "login"
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("login") {

            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {

                    navController.navigate("dashboard") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("dashboard") {

            DashboardScreen()
        }

        composable("shopping") {
            ShoppingScreen()
        }
    }
}