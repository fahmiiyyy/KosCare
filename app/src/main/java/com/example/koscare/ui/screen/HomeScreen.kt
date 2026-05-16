package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald


@Composable
fun HomeScreen(

    onNavigate: (String) -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxSize()
                .background(

                    Brush.verticalGradient(

                        colors = listOf(

                            Color(0xFFF4FFF8),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
    ) {
        Row(

            modifier =
                Modifier
                    .fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Card(

                shape =
                    RoundedCornerShape(18.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
            ) {

                Row(

                    modifier =
                        Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 10.dp
                        ),

                    verticalAlignment =
                        Alignment.CenterVertically
                ) {


                    Spacer(
                        modifier =
                            Modifier.width(10.dp)
                    )

                    Text(

                        text = "KosCare",

                        fontSize = 38.sp,

                        fontWeight =
                            FontWeight.ExtraBold,

                        color =
                            Emerald
                    )
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(34.dp)
        )

        Text(

            text =
                "Halo Mahasiswa 👋",

            style =
                MaterialTheme
                    .typography
                    .headlineMedium,

            fontWeight =
                FontWeight.ExtraBold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(

            text =
                "Kelola kebutuhan kosmu dengan mudah.",

            color =
                Color.Gray,

            style =
                MaterialTheme
                    .typography
                    .bodyLarge
        )

        Spacer(
            modifier =
                Modifier.height(32.dp)
        )

        HomeMenuCard(

            title =
                "Expense Tracker",

            subtitle =
                "Monitor rent, utilities, and daily spending.",

            icon =
                Icons.Default.Payments,

            iconBackground =
                Emerald,

            onClick = {

                onNavigate("expense")
            }
        )

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        HomeMenuCard(

            title =
                "Schedule Tracker",

            subtitle =
                "Manage cleaning duties and quiet hours.",

            icon =
                Icons.Default.CalendarMonth,

            iconBackground =
                Color(0xFFB7F0D8),

            onClick = {

                onNavigate("schedule")
            }
        )

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        HomeMenuCard(

            title =
                "Shopping List",

            subtitle =
                "Coordinate communal groceries and supplies.",

            icon =
                Icons.Default.ShoppingCart,

            iconBackground =
                Color(0xFF8FAF8F),

            onClick = {

                onNavigate("shopping")
            }
        )
    }
}

@Composable
fun HomeMenuCard(

    title: String,

    subtitle: String,

    icon: ImageVector,

    iconBackground: Color,

    onClick: () -> Unit
) {

    Card(

        modifier =
            Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 12.dp,
                    shape =
                        RoundedCornerShape(28.dp)
                )
                .clickable {

                    onClick()
                },

        shape =
            RoundedCornerShape(28.dp),

        colors =
            CardDefaults.cardColors(

                containerColor =
                    Color.White
            )
    ) {

        Box(

            modifier =
                Modifier.background(

                    brush =
                        Brush.horizontalGradient(

                            colors = listOf(

                                Color.White,

                                Emerald.copy(
                                    alpha = 0.06f
                                )
                            )
                        )
                )
        ) {

            Row(

                modifier =
                    Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 22.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(

                    modifier =
                        Modifier
                            .size(58.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = CircleShape
                            )
                            .background(
                                iconBackground,
                                CircleShape
                            ),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(

                        imageVector = icon,

                        contentDescription = null,

                        tint = Color.White,

                        modifier =
                            Modifier.size(28.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(18.dp)
                )

                Column {

                    Text(

                        text = title,

                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(4.dp)
                    )

                    Text(

                        text = subtitle,

                        color =
                            Color.Gray,

                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium
                    )
                }
            }
        }
    }
}