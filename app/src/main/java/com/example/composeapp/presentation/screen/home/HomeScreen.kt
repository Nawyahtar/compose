package com.example.composeapp.presentation.screen.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.R
import com.example.composeapp.presentation.components.buttons.CustomButton
import com.example.composeapp.presentation.theme.ComposeAppTheme
import com.example.composeapp.presentation.theme.HeartColor
import com.example.composeapp.utils.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.HomeUiEvent.NavigateToJournalScreen -> {
                    navController.navigate("journal")
                }

                is HomeViewModel.HomeUiEvent.ShowToast -> {
                    Toast.makeText(context, "Love you ❤️", Toast.LENGTH_SHORT).show()
                }

                is HomeViewModel.HomeUiEvent.NavigateToGalleryScreen -> {
                    navController.navigate("gallery")
                }

                is HomeViewModel.HomeUiEvent.NavigateToCountDownScreen -> {
                    navController.navigate("countdown")
                }

                is HomeViewModel.HomeUiEvent.NavigateToLoveQuotesScreen -> {
                    navController.navigate("loveQuotes")
                }

                is HomeViewModel.HomeUiEvent.NavigateToTestUiScreen -> {
                    navController.navigate(Screen.Test.route)
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HeartIcon { viewModel.onHeartIconClick() }
        Spacer(modifier = Modifier.height(12.dp))
        GreetingLove()
        Spacer(modifier = Modifier.height(24.dp))
        CustomButton("Journal") { viewModel.onJournalClick() }
        CustomButton("Gallery") { viewModel.onGalleryClick()}
        CustomButton("Countdown") { viewModel.onCountDownClick() }
        CustomButton("Love Quotes") { viewModel.onLoveQuotesClick() }
    }
}

@Composable
private fun HeartIcon(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(140.dp)
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.heart),
                contentDescription = "Heart Icon",
                tint = HeartColor,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Tap me",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
    }
}



@Composable
private fun GreetingLove() {
    Text(
        text = "Good Morning,\nLove!",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewHomeScreen() {
    ComposeAppTheme {
        HomeScreen(navController = rememberNavController())
    }
}
