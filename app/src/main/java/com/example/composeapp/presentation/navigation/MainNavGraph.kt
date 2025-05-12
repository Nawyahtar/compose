package com.example.composeapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.composeapp.presentation.screen.countdown.CountdownScreen
import com.example.composeapp.presentation.screen.gallery.GalleryScreen
import com.example.composeapp.presentation.screen.home.HomeScreen
import com.example.composeapp.presentation.screen.journal.JournalScreen
import com.example.composeapp.presentation.screen.loveQuotes.LoveQuotesScreen
import com.example.composeapp.utils.Screen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    navigation(startDestination = Screen.Home.route, route = "main") {
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Journal.route) { JournalScreen(navController) }
        composable(Screen.Gallery.route) { GalleryScreen(navController) }
        composable(Screen.Countdown.route) { CountdownScreen(navController) }
        composable(Screen.LoveQuotes.route) { LoveQuotesScreen(navController) }
    }
}