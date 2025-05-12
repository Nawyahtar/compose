package com.example.composeapp.presentation.screen.gallery

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.composeapp.data.local.entity.PhotoEntity
import com.example.composeapp.presentation.components.alerts.CustomDeleteAlert
import com.example.composeapp.presentation.components.screenTitle.ScreenTitle
import com.example.composeapp.presentation.screen.gallery.GalleryViewModel.GalleryUIState
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GalleryScreen(navController: NavController, viewModel: GalleryViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var photoToDelete by remember { mutableStateOf<PhotoEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.confirmDeleteEvent.collectLatest { event ->
            when (event) {
                is GalleryViewModel.GalleryUIEvent.ShowDeleteAlert -> {
                    photoToDelete = event.photo
                }
            }
        }
    }

    photoToDelete?.let { photo ->
        CustomDeleteAlert(
            titleText = "Delete Photo",
            messageText = "Are you sure you want to delete this photo?",
            onConfirm = {
                viewModel.deletePhoto(photo)
                photoToDelete = null
            },
            onCancel = { photoToDelete = null }
        )
    }
    if (uiState is GalleryUIState.Loading) {
        FullScreenLoader()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenTitle(title = "Gallery") { navController.popBackStack() }

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            is GalleryUIState.Success -> {
                val photoList = (uiState as GalleryUIState.Success).photos

                if (photoList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No photos available", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(photoList) { photo ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(4.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(photo.photoUri.toUri()),
                                    contentDescription = null,
                                    contentScale = ContentScale.FillBounds,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.medium)
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(6.dp)
                                        .size(24.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(
                                        onClick = { viewModel.requestDelete(photo) },
                                        modifier = Modifier.size(20.dp),
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is GalleryUIState.Error -> {
                val message = (uiState as GalleryUIState.Error).message
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = message, color = MaterialTheme.colorScheme.error)
                }
            }

            else -> Unit
        }
    }
}

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

