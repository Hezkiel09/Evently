package com.example.evently.features.announcement.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.announcement.data.model.Announcement

private val PrimaryOrange = Color(0xFFFF7A00)
private val DarkPurple = Color(0xFF3B1C5A)
private val MediumPurple = Color(0xFF7B42B3)
private val LightBg = Color(0xFFF8F9FA)

@Composable
fun AnnouncementDetailScreen(
    announcement: Announcement,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold(
        containerColor = LightBg,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MediumPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Rounded.Delete, contentDescription = "Hapus", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hapus")
                }
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MediumPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Rounded.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(DarkPurple) // Placeholder for Image
            ) {
                Column(modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                                .background(Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Text("Detail Pengumuman", color = Color.White, fontWeight = FontWeight.SemiBold)
                        Text("Evently.", color = Color.White, fontWeight = FontWeight.ExtraBold)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Campaign, contentDescription = null, tint = PrimaryOrange, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("KABAR DARI EVENT KAMU", color = PrimaryOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(announcement.event, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }

            // Overlapping Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-40).dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(announcement.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = DarkPurple, lineHeight = 28.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Meta Info
                    Row {
                        Box(modifier = Modifier.size(36.dp).background(Color(0x17FF7A00), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.Campaign, contentDescription = null, tint = PrimaryOrange)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Event", color = Color.Gray, fontSize = 10.sp)
                            Text(announcement.event, color = DarkPurple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Box(modifier = Modifier.size(36.dp).background(Color(0x17FF7A00), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Rounded.DateRange, contentDescription = null, tint = PrimaryOrange)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Waktu", color = Color.Gray, fontSize = 10.sp)
                            Text(announcement.date, color = DarkPurple, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Detail Content
            Column(modifier = Modifier.padding(horizontal = 24.dp).offset(y = (-20).dp)) {
                Text("Detail Informasi", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkPurple)
                Spacer(modifier = Modifier.height(12.dp))
                Text(announcement.content, color = Color.DarkGray, fontSize = 14.sp, lineHeight = 22.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}