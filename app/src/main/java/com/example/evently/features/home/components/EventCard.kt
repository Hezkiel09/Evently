package com.example.evently.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.home.presentation.EventUiModel

private val EventlyOrange = Color(0xFFF59E0B)

@Composable
fun EventCard(
    event: EventUiModel
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column {

            // IMAGE / BANNER EVENT
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when (event.category) {
                            "Music" -> Color(0xFF173B38)
                            "Education" -> Color(0xFFD9D9BC)
                            else -> Color.LightGray
                        }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "EVENTLY LIVE SESSIONS",
                        fontSize = 7.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (event.category) {
                            "Music" -> "SUNSET\nSESSIONS."
                            "Education" -> "Creative\nMeetup."
                            else -> event.title
                        },
                        fontSize = 24.sp,
                        lineHeight = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (event.category == "Education") {
                            Color(0xFF29473F)
                        } else {
                            Color.White
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "${event.date.uppercase()} / JAKARTA",
                        fontSize = 7.sp,
                        color = if (event.category == "Education") {
                            Color(0xFF29473F)
                        } else {
                            Color.White
                        }
                    )
                }
            }

            // EVENT INFO
            Column(
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 10.dp
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFFFF0D2),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = event.category,
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 5.dp
                            ),
                            fontSize = 10.sp,
                            color = Color(0xFF9A5B00)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${event.date} · ${event.time}",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "More"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color.Gray
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = event.location,
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Diselenggarakan oleh ${event.organizer}",
                        fontSize = 9.sp,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EventlyOrange
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Join",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}