package com.example.composeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import com.example.composeapp.presentation.navigation.AppNavigation
import com.example.composeapp.presentation.theme.ComposeAppTheme
import com.example.composeapp.presentation.theme.HeartColor
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppTheme {
                val color = HeartColor.toArgb()
                val window = (this).window
                SideEffect {
                    window.statusBarColor = color
                }
                AppNavigation()
            }
        }
    }
}