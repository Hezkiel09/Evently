package com.example.evently.features.crudevent.presentation


import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val EventlyOrange = Color(0xFFF59E0B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventScreen(
    viewModel: AddEditViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val event = uiState.event

    val title = if (uiState.isEditMode) {
        "Edit Event"
    } else {
        "Tambah Event"
    }

    Scaffold(
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
                text = "Poster Event *",
                fontSize = 11.sp
            )

            PosterPicker(
                poster = event.poster,
                onPosterSelected = {
                    viewModel.updatePoster(it)
                }
            )

            EventTextField(
                label = "Nama Event *",
                value = event.name,
                placeholder = "Masukkan nama event",
                onValueChange = {
                    viewModel.updateName(it)
                }
            )

            CategoryDropdown(
                value = event.category,
                onValueChange = {
                    viewModel.updateCategory(it)
                }
            )

            EventTextField(
                label = "Tanggal & Waktu *",
                value = buildString {
                    append(event.date)

                    if (event.date.isNotEmpty() && event.time.isNotEmpty()) {
                        append(", ")
                    }

                    append(event.time)
                },
                placeholder = "Pilih tanggal dan waktu",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null
                    )
                },
                onValueChange = {}
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
                    viewModel.saveEvent()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EventlyOrange
                )
            ) {
                Text(
                    text = "Simpan Event",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
private fun PosterPicker(
    poster: String?,
    onPosterSelected: (String) -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(
                color = Color(0xFFFFFCF5),
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFF2D9A0),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = EventlyOrange
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = if (poster == null) {
                    "Pilih poster event"
                } else {
                    "Poster dipilih"
                },
                fontSize = 10.sp
            )

            Text(
                text = "JPG, PNG atau WebP • Maks. 5 MB",
                fontSize = 8.sp,
                color = Color.Gray
            )
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
    minLines: Int = 1
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
            shape = RoundedCornerShape(8.dp)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    value: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val categories = listOf(
        "Music",
        "Education",
        "Film",
        "Workshop",
        "Seminar"
    )

    Column {
        Text(
            text = "Kategori *",
            fontSize = 10.sp
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                placeholder = {
                    Text(
                        text = "Pilih kategori",
                        fontSize = 11.sp
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                shape = RoundedCornerShape(8.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(category)
                        },
                        onClick = {
                            onValueChange(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}