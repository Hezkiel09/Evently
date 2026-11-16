package com.example.evently.features.announcement.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.announcement.data.model.Announcement
import com.example.evently.features.announcement.data.model.EventDto

private val PrimaryOrange = Color(0xFFFF7A00)
private val SecondaryYellow = Color(0xFFFFB800)
private val DarkPurple = Color(0xFF3B1C5A)
private val MediumPurple = Color(0xFF7B42B3)
private val LightBg = Color(0xFFF8F9FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementFormScreen(
    initialAnnouncement: Announcement? = null,
    eventOptions: List<EventDto> = emptyList(),
    onBackClick: () -> Unit,
    onSubmit: (title: String, eventId: Long, content: String) -> Unit
) {
    val isEditMode = initialAnnouncement != null

    var title by rememberSaveable(initialAnnouncement) { mutableStateOf(initialAnnouncement?.title ?: "") }
    var content by rememberSaveable(initialAnnouncement) { mutableStateOf(initialAnnouncement?.content ?: "") }

    var selectedEventId by rememberSaveable(initialAnnouncement) {
        mutableStateOf(initialAnnouncement?.eventId ?: eventOptions.firstOrNull()?.id ?: 0L)
    }
    var selectedEventTitle by rememberSaveable(initialAnnouncement) {
        mutableStateOf(initialAnnouncement?.event ?: eventOptions.firstOrNull()?.title ?: "")
    }
    var expanded by rememberSaveable { mutableStateOf(false) }

    // Efek Sinkronisasi: Update dropdown ketika data event dari Supabase selesai dimuat
    LaunchedEffect(eventOptions, selectedEventId) {
        if (selectedEventId == 0L && eventOptions.isNotEmpty()) {
            selectedEventId = eventOptions.first().id
            selectedEventTitle = eventOptions.first().title
        } else if (selectedEventTitle.isEmpty() && eventOptions.isNotEmpty()) {
            val matchingEvent = eventOptions.find { it.id == selectedEventId }
            if (matchingEvent != null) {
                selectedEventTitle = matchingEvent.title
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.background(LightBg, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = DarkPurple)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("EVENTLY • PENGUMUMAN", color = PrimaryOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(if (isEditMode) "Edit Pengumuman" else "Buat Pengumuman Baru", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkPurple)
                    }
                }
                Divider(color = Color(0xFFF0F0F0))
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Informasi akan dibagikan kepada peserta event.", color = Color.Gray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (selectedEventId != 0L && title.isNotBlank() && content.isNotBlank()) {
                            onSubmit(title, selectedEventId, content)
                        }
                    },
                    enabled = selectedEventId != 0L && title.isNotBlank() && content.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(Brush.linearGradient(listOf(PrimaryOrange, SecondaryYellow)), RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Text(
                        if (isEditMode) "Simpan Perubahan >" else "Kirim Pengumuman >",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8F5FC), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFEFE8F5), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(Color.White, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Campaign, contentDescription = null, tint = MediumPurple)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(if (isEditMode) "Kabar baik selalu jelas." else "Beri kabar, tetap terhubung.", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkPurple)
                    Text("Pastikan informasi yang disampaikan singkat, jelas, dan mudah dipahami.", fontSize = 11.sp, color = Color.Gray, lineHeight = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Pilih Event *", fontWeight = FontWeight.Bold, color = DarkPurple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedEventTitle.ifEmpty { "Pilih event terkait" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = PrimaryOrange
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    eventOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.title) },
                            onClick = {
                                selectedEventId = option.id
                                selectedEventTitle = option.title
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("Judul Pengumuman *", fontWeight = FontWeight.Bold, color = DarkPurple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { if (it.length <= 80) title = it },
                placeholder = { Text("Contoh: Perubahan jadwal acara") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = PrimaryOrange
                )
            )
            Text("${title.length}/80", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.align(Alignment.End).padding(top = 4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            Text("Isi Pengumuman *", fontWeight = FontWeight.Bold, color = DarkPurple, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { if (it.length <= 500) content = it },
                placeholder = { Text("Tulis detail informasi yang ingin disampaikan...") },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedBorderColor = PrimaryOrange
                )
            )
            Text("${content.length}/500", color = Color.Gray, fontSize = 10.sp, modifier = Modifier.align(Alignment.End).padding(top = 4.dp))

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}