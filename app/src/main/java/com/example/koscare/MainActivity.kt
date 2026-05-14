package com.example.koscare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.ui.theme.KosCareTheme
import android.util.Log
import com.example.koscare.data.remote.SupabaseClientProvider

import com.example.koscare.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("SUPABASE", SupabaseClientProvider.client.toString())

        setContent {
            KosCareTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun HomePreview() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "KosCare",
            color = Emerald,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Aplikasi Management Anak Kos",
            fontSize = 16.sp
        )
    }
}