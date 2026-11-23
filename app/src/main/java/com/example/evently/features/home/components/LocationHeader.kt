package com.example.evently.features.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val EventlyOrange = Color(0xFFF59E0B)

@Composable
fun LocationHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Lokasi",
            tint = EventlyOrange,
            modifier = Modifier.size(25.dp)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Lokasi kamu",
                fontSize = 10.sp,
                color = Color.Gray
            )

            Text(
                text = "Jakarta, Indonesia",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = {}
        ) {

            Icon(
                imageVector = Icons.Default.NotificationsNone,
                contentDescription = "Notifikasi"
            )
        }
    }
}