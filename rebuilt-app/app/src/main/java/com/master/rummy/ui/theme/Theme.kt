package com.master.rummy.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = Color.Black,
    secondary = FeltGreenLight,
    onSecondary = Color.White,
    background = FeltGreenDark,
    onBackground = Color.White,
    surface = FeltGreenPrimary,
    onSurface = Color.White
)

@Composable
fun MasterRummyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
