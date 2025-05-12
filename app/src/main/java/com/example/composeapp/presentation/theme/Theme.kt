package com.example.composeapp.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RomanticColorScheme = lightColorScheme(
    primary = RomanticPink,
    secondary = SoftPink,
    tertiary = LightRose,
    background = CreamWhite,
    surface = PureWhite,
    onPrimary = WhiteText,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
)


@Composable
fun ComposeAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = RomanticColorScheme,
        typography = AppTypography,
        content = content
    )
}
