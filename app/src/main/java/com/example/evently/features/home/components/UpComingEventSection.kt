package com.example.evently.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.home.presentation.EventUiModel

private val EventlyOrange = Color(0xFFF59E0B)

@Composable
fun UpComingEventSection(
    events: List<EventUiModel>,
    userRole: String = "student",
    onAddEventClick: () -> Unit,
    onEditEventClick: (EventUiModel) -> Unit = {},
    onDeleteEventClick: (EventUiModel) -> Unit = {}
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Upcoming Event",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            if (userRole in setOf("organizer", "admin")) {
                Button(
                    onClick = onAddEventClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF0D2),
                        contentColor = Color(0xFF9A5B00)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(
                        modifier = Modifier.width(4.dp)
                    )

                    Text(
                        text = "Tambah",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // EVENT LIST
        if (events.isEmpty()) {
            Text("Belum ada event.", color = Color.Gray, fontSize = 13.sp)
        }
        events.forEach { event ->

            EventCard(
                event = event,
                userRole = userRole,
                onEditClick = { onEditEventClick(event) },
                onDeleteClick = { onDeleteEventClick(event) }
            )

            Spacer(
                modifier = Modifier.height(14.dp)
            )
        }
    }
}
