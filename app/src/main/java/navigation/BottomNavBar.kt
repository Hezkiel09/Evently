package com.example.evently.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ActiveColor = Color(0xFFF59E0B)
private val InactiveColor = Color(0xFFA8A0B3)

data class BottomNavItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar() {

    val items = listOf(
        BottomNavItem(
            label = "Beranda",
            icon = Icons.Outlined.Home
        ),
        BottomNavItem(
            label = "Jelajah",
            icon = Icons.Outlined.Explore
        ),
        BottomNavItem(
            label = "Tiket",
            icon = Icons.Outlined.ConfirmationNumber
        ),
        BottomNavItem(
            label = "Pengumuman",
            icon = Icons.Outlined.Campaign
        ),
        BottomNavItem(
            label = "Profil",
            icon = Icons.Outlined.Person
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE0E3E8),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        items.forEachIndexed { index, item ->

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(78.dp)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                val isActive = index == 0

                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isActive) {
                        ActiveColor
                    } else {
                        InactiveColor
                    }
                )

                Text(
                    text = item.label,
                    fontSize = 12.sp,
                    color = if (isActive) {
                        ActiveColor
                    } else {
                        InactiveColor
                    }
                )
            }
        }
    }
}