package com.example.koscare.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.koscare.ui.screen.DashboardScreen
import com.example.koscare.ui.screen.LoginScreen
import com.example.koscare.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val authViewModel = remember {
        AuthViewModel()
    }

    var isLoggedIn by remember {
        mutableStateOf(
            authViewModel.isUserLoggedIn()
        )
    }

    NavHost(
        navController = navController,

        startDestination =
            if (isLoggedIn)
                "dashboard"
            else
                "login"
    ) {

        composable("login") {

            LoginScreen(

                authViewModel = authViewModel,

                onLoginSuccess = {

                    isLoggedIn = true

                    navController.navigate("dashboard") {

                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("dashboard") {

            DashboardScreen(
                rootNavController = navController
            )
        }
    }
}