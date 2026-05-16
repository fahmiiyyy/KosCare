package com.example.koscare.navigation

import androidx.compose.runtime.*
import com.example.koscare.ui.screen.DashboardScreen
import com.example.koscare.ui.screen.LoginScreen
import com.example.koscare.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {

    val authViewModel = remember {
        AuthViewModel()
    }

    var isLoggedIn by remember {
        mutableStateOf(
            authViewModel.isUserLoggedIn()
        )
    }

    if (isLoggedIn) {

        DashboardScreen(

            onLogout = {
                authViewModel.logout()  // ← reset _isSuccess di dalam sini
                isLoggedIn = false
            }
        )

    } else {

        LoginScreen(

            authViewModel = authViewModel,

            onLoginSuccess = {
                isLoggedIn = true
            }
        )
    }
}