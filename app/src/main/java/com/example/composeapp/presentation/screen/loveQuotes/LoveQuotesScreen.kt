package com.example.composeapp.presentation.screen.loveQuotes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.composeapp.presentation.components.screenTitle.ScreenTitle

@Composable
fun LoveQuotesScreen(
    navController: NavController,
    viewModel: LoveQuoteViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
                )
            )
            .padding(16.dp)
    ) {
        ScreenTitle(title = "Words from the Heart") { navController.popBackStack() }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (state) {
                is LoveQuoteViewModel.LoveQuoteUiState.Loading -> {
                    CircularProgressIndicator(color = Color.Red)
                }

                is LoveQuoteViewModel.LoveQuoteUiState.Success -> {
                    val data = state as LoveQuoteViewModel.LoveQuoteUiState.Success
                    QuoteCard(quote = data.quote, author = data.author)
                }

                is LoveQuoteViewModel.LoveQuoteUiState.Error -> {
                    val message = (state as LoveQuoteViewModel.LoveQuoteUiState.Error).message
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteCard(quote: String, author: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "💕 Today's Love Quote",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
            color = Color.Red,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "\"$quote\"",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            ),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "- $author",
            style = MaterialTheme.typography.bodySmall.copy(
                fontStyle = FontStyle.Italic,
                color = Color.DarkGray
            ),
            modifier = Modifier.align(Alignment.End)
        )
    }
}
