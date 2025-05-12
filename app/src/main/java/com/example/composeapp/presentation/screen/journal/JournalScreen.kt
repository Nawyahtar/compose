package com.example.composeapp.presentation.screen.journal

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.composeapp.R
import com.example.composeapp.presentation.components.buttons.ButtonWithIcon
import com.example.composeapp.presentation.components.buttons.CustomButton
import com.example.composeapp.presentation.components.screenTitle.ScreenTitle
import com.example.composeapp.presentation.theme.ComposeAppTheme
import kotlinx.coroutines.flow.collectLatest


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(
    navController: NavController, viewModel: JournalViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pickedImageUri = remember { mutableStateOf<Uri?>(null) }

    val openPhotoPicker = rememberPhotoPicker { uri ->
        pickedImageUri.value = uri
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is JournalViewModel.JournalUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                JournalViewModel.JournalUiEvent.SaveSuccess -> {
                    Toast.makeText(context, "Saved Photo", Toast.LENGTH_SHORT).show()
                    pickedImageUri.value = null
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ScreenTitle(title = viewModel.formattedDate ?: "") { navController.popBackStack() }

        Spacer(modifier = Modifier.height(32.dp))

        JournalMessageCard(message = stringResource(id = R.string.journal_message))
        pickedImageUri.value?.let { uri ->
            SelectedImagePreview(
                imageUri = uri,
                onRemove = { pickedImageUri.value = null }
            )
        }

        Spacer(modifier = Modifier.height(100.dp))

        if (pickedImageUri.value == null) {
            ButtonWithIcon(label = "Add Photo") {
                openPhotoPicker()
            }
        }

        CustomButton(label = "Save Memory") {
            viewModel.onSaveMemoryClick(uri = pickedImageUri.value)
        }
    }

}

@Composable
fun SelectedImagePreview(
    imageUri: Uri,
    onRemove: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(top = 16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))

        )

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(
                    Color.Black.copy(alpha = 0.4f),
                    shape = androidx.compose.foundation.shape.CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove image",
                tint = Color.White
            )
        }
    }
}


@Composable
fun rememberPhotoPicker(onPhotoPicked: (Uri) -> Unit): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                // Persist read permission
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                Log.e("PhotoPicker", "Failed to persist URI permission", e)
            }

            Log.i("PhotoPicker", "Picked image URI: $uri")
            onPhotoPicked(it)
        }
    }

    return {
        launcher.launch("image/*")
    }
}

@Composable
private fun JournalMessageCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0E8F4), // optional: light purple
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewJournalScreen() {
    ComposeAppTheme {
        JournalScreen(navController = rememberNavController())
    }
}
