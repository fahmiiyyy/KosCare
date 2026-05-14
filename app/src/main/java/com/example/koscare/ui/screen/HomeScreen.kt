package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald

@Composable
fun HomeScreen(

    onNavigate: (String) -> Unit
) {

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
            text = "Home Page",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        HomeMenuCard(

            modifier =
                Modifier.fillMaxWidth(),

            title = "Expense",

            icon =
                Icons.Default.Payments,

            onClick = {

                onNavigate("expense")
            }
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        HomeMenuCard(

            modifier =
                Modifier.fillMaxWidth(),

            title = "Schedule",

            icon =
                Icons.Default.CalendarMonth,

            onClick = {

                onNavigate("schedule")
            }
        )

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

    icon: ImageVector,

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
                Modifier.padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),

            horizontalAlignment =
                Alignment.Start,

            verticalArrangement =
                Arrangement.Center
        ) {

            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = Emerald,

                modifier =
                    Modifier.size(36.dp)
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
                        .titleLarge,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}