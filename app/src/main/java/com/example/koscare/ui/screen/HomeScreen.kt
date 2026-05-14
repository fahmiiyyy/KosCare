package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.HomeViewModel

@Composable
fun HomeScreen(

    onNavigate: (String) -> Unit,

    viewModel: HomeViewModel = viewModel()
) {

    val uiState by
    viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {

        viewModel.loadDashboard()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(20.dp)
    ) {

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                "Halo, ${uiState.userName} 👋",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Card(

            modifier =
                Modifier.fillMaxWidth(),

            shape =
                RoundedCornerShape(28.dp),

            colors =
                CardDefaults.cardColors(
                    containerColor =
                        Emerald
                )
        ) {

            Column(
                modifier =
                    Modifier.padding(24.dp)
            ) {

                Text(
                    text =
                        "Total Pengeluaran",

                    color = Color.White
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "Rp ${uiState.totalExpense.toInt()}",

                    style =
                        MaterialTheme
                            .typography
                            .headlineMedium,

                    color = Color.White,

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            HomeMenuCard(

                modifier =
                    Modifier.weight(1f),

                title = "Expense",

                icon =
                    Icons.Default.Payments,

                onClick = {

                    onNavigate("expense")
                }
            )

            HomeMenuCard(

                modifier =
                    Modifier.weight(1f),

                title = "Schedule",

                icon =
                    Icons.Default.CalendarMonth,

                onClick = {

                    onNavigate("schedule")
                }
            )
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        HomeMenuCard(

            modifier =
                Modifier.fillMaxWidth(),

            title = "Shopping",

            icon =
                Icons.Default.ShoppingCart,

            onClick = {

                onNavigate("shopping")
            }
        )
    }
}

@Composable
fun HomeMenuCard(

    modifier: Modifier = Modifier,

    title: String,

    icon: androidx.compose.ui.graphics.vector.ImageVector,

    onClick: () -> Unit
) {

    Card(

        modifier =
            modifier.clickable {

                onClick()
            },

        shape =
            RoundedCornerShape(24.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            )
    ) {

        Column(

            modifier =
                Modifier.padding(24.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = Emerald,

                modifier =
                    Modifier.size(40.dp)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(

                text = title,

                style =
                    MaterialTheme
                        .typography
                        .titleMedium,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}