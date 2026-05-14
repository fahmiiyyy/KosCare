package com.example.koscare.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = lightColorScheme(

    primary = Emerald,

    secondary = EmeraldDark,

    tertiary = EmeraldLight,

    background = Background,

    surface = CardBackground,

    onPrimary = Color.White,

    onBackground = TextPrimary,

    onSurface = TextPrimary
)

@Composable
fun KosCareTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(

        colorScheme =
            AppColorScheme,

        content = content
    )
}