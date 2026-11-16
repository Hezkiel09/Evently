package com.example.evently.features.announcement.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import com.example.evently.core.ui.components.EventlyNavigationBar
import com.example.evently.core.ui.components.EventlyNavItem
import com.example.evently.features.announcement.data.model.Announcement

// --- Warna Sesuai Desain Figma ---
private val PrimaryOrange = Color(0xFFFF7A00)
private val SecondaryYellow = Color(0xFFFFB800)
private val DarkPurple = Color(0xFF3B1C5A)
private val MediumPurple = Color(0xFF7B42B3)
private val LightBg = Color(0xFFF8F9FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementFeedScreen(
    announcements: List<Announcement>,
    onAnnouncementClick: (Announcement) -> Unit,
    onFabClick: () -> Unit
) {
    Scaffold(
        containerColor = LightBg,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo Text (Placeholder)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = "Logo", tint = PrimaryOrange)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Evently.", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = DarkPurple)
                    }
                    Text("YOUR CAMPUS,\nCONNECTED", fontSize = 8.sp, color = Color.Gray, fontWeight = FontWeight.Bold, lineHeight = 12.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Pengumuman", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = DarkPurple)
                        Text("Semua kabar event, dalam satu tempat.", fontSize = 12.sp, color = Color.Gray)
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .background(LightBg, RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Rounded.Notifications, contentDescription = "Notifikasi", tint = DarkPurple)
                    }
                }
            }
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(PrimaryOrange, SecondaryYellow)))
                    .clickable { onFabClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Tambah", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        },
        bottomBar = {
            EventlyNavigationBar(selectedItem = EventlyNavItem.ANNOUNCEMENT)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Kabar terbaru", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkPurple)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.background(Color(0xFFEDE7F6), RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("${announcements.size}", color = MediumPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Semua pengumuman", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(announcements) { item ->
                AnnouncementFeedCard(item) { onAnnouncementClick(item) }
            }
            item { Spacer(modifier = Modifier.height(80.dp)) } // Ruang untuk FAB
        }
    }
}

@Composable
fun AnnouncementFeedCard(announcement: Announcement, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge
                val badgeBg = if (announcement.urgent) Color(0x17FF7A00) else Color(0x147B42B3)
                val badgeColor = if (announcement.urgent) PrimaryOrange else MediumPurple
                Row(
                    modifier = Modifier.background(badgeBg, RoundedCornerShape(6.dp)).padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(badgeColor))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (announcement.urgent) "Penting" else "Informasi", color = badgeColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Text(announcement.time, color = Color.Gray, fontSize = 10.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                // Placeholder Image
                Box(modifier = Modifier.size(60.dp).background(Color.LightGray, RoundedCornerShape(12.dp)))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(announcement.title, fontWeight = FontWeight.Bold, color = DarkPurple, fontSize = 16.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = PrimaryOrange, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(announcement.event, color = MediumPurple, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(announcement.preview, color = Color.DarkGray, fontSize = 13.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(announcement.location, color = Color.Gray, fontSize = 11.sp)
                Text("Lihat detail >", color = PrimaryOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}