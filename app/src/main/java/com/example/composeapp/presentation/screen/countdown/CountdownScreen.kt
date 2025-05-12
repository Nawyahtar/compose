package com.example.composeapp.presentation.screen.countdown

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.composeapp.presentation.components.buttons.ButtonWithIcon
import com.example.composeapp.presentation.components.buttons.CustomButton
import com.example.composeapp.presentation.components.screenTitle.ScreenTitle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownScreen(navController: NavController, viewModel: CountdownViewModel = hiltViewModel()) {
    val eventName by viewModel.eventName
    val eventDate by viewModel.eventDate
    val uiState by viewModel.uiState.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    var isSheetOpen by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val tomorrowMillis = remember {
        val tomorrow = LocalDate.now().plusDays(1)
        tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= tomorrowMillis
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is CountdownViewModel.CountdownUIEvent.OpenAddEventSheet -> isSheetOpen = true
                is CountdownViewModel.CountdownUIEvent.CloseAddEventSheet -> isSheetOpen = false
                is CountdownViewModel.CountdownUIEvent.OpenDatePicker -> showDatePicker = true
                is CountdownViewModel.CountdownUIEvent.ShowSnackBar -> {
                    val job = launch {
                        snackBarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                    delay(1000)
                    job.cancel()
                    snackBarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        },
        floatingActionButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                ButtonWithIcon(
                    label = "Add Event",
                    icon = Icons.Default.Add,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(0.9f)
                ) {
                    viewModel.onAddEventClicked()
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            ScreenTitle("Countdown") { navController.popBackStack() }

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is CountdownViewModel.CountdownUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is CountdownViewModel.CountdownUiState.Error -> {
                    val error = (uiState as CountdownViewModel.CountdownUiState.Error).message
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: $error", color = Color.Red)
                    }
                }

                is CountdownViewModel.CountdownUiState.Success -> {
                    val eventList = (uiState as CountdownViewModel.CountdownUiState.Success).events
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(eventList) { event ->
                            EventCard(
                                title = "🎉 ${event.name}",
                                eventDate = event.date
                            )
                        }
                    }
                }
            }
        }

        // Bottom Sheet
        if (isSheetOpen) {
            AddEventBottomSheet(
                eventName = eventName,
                onEventNameChange = viewModel::onEventNameChanged,
                eventDate = eventDate,
                onEventDateChange = {},
                onSave = viewModel::onSave,
                onDismiss = viewModel::onDismissSheet,
                onOpenDatePicker = viewModel::onRequestDatePicker
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            viewModel.onDatePicked(it)
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventBottomSheet(
    eventName: String,
    onEventNameChange: (String) -> Unit,
    eventDate: String,
    onEventDateChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    onOpenDatePicker: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color(0xFFFFF8F9)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Add Countdown Event", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = eventName,
                onValueChange = onEventNameChange,
                label = { Text("Event Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = eventDate,
                onValueChange = {},
                label = { Text("Event Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = onOpenDatePicker) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select Date"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            CustomButton(label = "Save", modifier = Modifier.height(80.dp)) {
                onSave()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun calculateDaysUntil(dateString: String): Long {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val eventDate = LocalDate.parse(dateString, formatter)
        val today = LocalDate.now()
        ChronoUnit.DAYS.between(today, eventDate)
    } catch (e: Exception) {
        0L // fallback in case of parsing error
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventCard(
    title: String,
    eventDate: String,
    modifier: Modifier = Modifier
) {
    val today = remember { LocalDate.now() }
    val daysBetween = remember(eventDate) {
        try {
            val date = LocalDate.parse(eventDate)
            ChronoUnit.DAYS.between(today, date).toInt()
        } catch (e: Exception) {
            null
        }
    }

    val (dayText, background) = when {
        daysBetween == null -> "Invalid date" to Color.LightGray
        daysBetween < 0 -> "${-daysBetween} days ago" to Color(0xFFFF746C)
        daysBetween == 0 -> "Today!" to Color.White
        else -> "In $daysBetween days" to Color.White
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = background,
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(color = Color.Black)
            )
            Text(
                text = dayText,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
            )
        }
    }
}






