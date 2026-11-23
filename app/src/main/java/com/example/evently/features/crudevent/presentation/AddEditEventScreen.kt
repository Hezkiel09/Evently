package com.example.evently.features.crudevent.presentation

import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val EventlyOrange = Color(0xFFF59E0B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    viewModel: AddEditViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val event = uiState.event

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            onBackClick()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                            val formattedDate = formatter.format(Date(millis))
                            viewModel.updateDate(formattedDate)
                            if (event.eventDate.substringAfter('T', "").isBlank()) {
                                viewModel.updateTime("18:30")
                            }
                        }
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    val title = if (uiState.isEditMode) {
        "Edit Event"
    } else {
        "Tambah Event"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = "Buat momen yang layak dibagikan.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            Text(
                text = "URL Banner (opsional)",
                fontSize = 11.sp
            )

            EventTextField(
                label = "URL Banner",
                value = event.bannerUrl.orEmpty(),
                placeholder = "https://...",
                onValueChange = viewModel::updatePoster
            )

            EventTextField(
                label = "Nama Event *",
                value = event.name,
                placeholder = "Masukkan nama event",
                onValueChange = {
                    viewModel.updateName(it)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                EventTextField(
                    label = "Tanggal & Waktu *",
                    value = buildString {
                        val date = event.eventDate.substringBefore('T')
                        val time = event.eventDate.substringAfter('T', "").substringBefore(':', "") +
                            ":" + event.eventDate.substringAfter('T', "").substringAfter(':', "").take(2)
                        append(date)

                        if (date.isNotEmpty() && time.length > 1) {
                            append(", ")
                        }
                        append(time.trim(':'))
                    },
                    placeholder = "Pilih tanggal dan waktu",
                    trailingIcon = {
                        IconButton(
                            onClick = { showDatePicker = true }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.CalendarMonth,
                                contentDescription = null
                            )
                        }
                    },
                    onValueChange = {},
                    readOnly = true
                )
            }

            EventTextField(
                label = "Waktu *",
                value = event.eventDate.substringAfter('T', "").take(5),
                placeholder = "18:30",
                onValueChange = { value ->
                    val normalized = value.filter { it.isDigit() || it == ':' }.take(5)
                    if (normalized.length == 5) viewModel.updateTime(normalized)
                }
            )

            EventTextField(
                label = "Lokasi *",
                value = event.location,
                placeholder = "Nama tempat atau alamat",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null
                    )
                },
                onValueChange = {
                    viewModel.updateLocation(it)
                }
            )

            EventTextField(
                label = "Deskripsi *",
                value = event.description,
                placeholder = "Ceritakan hal menarik tentang eventmu...",
                minLines = 4,
                onValueChange = {
                    viewModel.updateDescription(it)
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    if (!uiState.isLoading) {
                        viewModel.saveEvent()
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventlyOrange
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Menyimpan...",
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "Simpan Event",
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
private fun EventTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    minLines: Int = 1,
    readOnly: Boolean = false
) {
    Column {

        Text(
            text = label,
            fontSize = 10.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 11.sp
                )
            },
            trailingIcon = trailingIcon,
            minLines = minLines,
            singleLine = minLines == 1,
            shape = RoundedCornerShape(8.dp),
            readOnly = readOnly
        )
    }
}
