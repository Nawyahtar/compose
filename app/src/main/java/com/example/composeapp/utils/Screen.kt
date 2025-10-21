package com.example.composeapp.utils

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home: Screen("home")
    object Journal: Screen("journal")
    object Gallery: Screen("gallery")
    object Countdown: Screen("countdown")
    object LoveQuotes: Screen("loveQuotes")
    object Test: Screen("test")
}