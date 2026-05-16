package com.example.koscare.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.koscare.navigation.BottomNavItem
import com.example.koscare.viewmodel.ProfileViewModel

@Composable
fun DashboardScreen(
    rootNavController: NavController
) {

    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Expense,
        BottomNavItem.Schedule,
        BottomNavItem.Shopping,
        BottomNavItem.Profile
    )

    Scaffold(

        bottomBar = {

            NavigationBar {

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->

                    NavigationBarItem(

                        selected = currentRoute == item.route,

                        onClick = {

                            navController.navigate(item.route) {

                                popUpTo(navController.graph.findStartDestination().id)

                                launchSingleTop = true
                            }
                        },

                        icon = {

                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },

                        label = {
                            Text(item.title)
                        }
                    )
                }
            }
        }

    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,

            modifier = Modifier.padding(innerPadding)
        ) {

            composable(BottomNavItem.Home.route) {

                HomeScreen(

                    onNavigate = {

                        navController.navigate(it)
                    }
                )
            }

            composable(BottomNavItem.Expense.route) {
                ExpenseScreen()
            }

            composable(BottomNavItem.Schedule.route) {
                ScheduleScreen()
            }

            composable(BottomNavItem.Profile.route) {

                val profileViewModel: ProfileViewModel =
                    viewModel()

                ProfileScreen(
                    navController = rootNavController,
                    viewModel = profileViewModel
                )
            }

            composable(BottomNavItem.Shopping.route) {
                ShoppingScreen()
            }
        }
    }
}