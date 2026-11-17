package com.example.evently.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Campaign
import androidx.compose.material.icons.rounded.ConfirmationNumber
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val PrimaryOrange = Color(0xFFFF7A00)

enum class EventlyNavItem {
    HOME,
    EXPLORE,
    TICKET,
    ANNOUNCEMENT,
    PROFILE
}

@Composable
fun EventlyNavigationBar(
    selectedItem: EventlyNavItem = EventlyNavItem.ANNOUNCEMENT,
    onItemSelected: (EventlyNavItem) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedItem == EventlyNavItem.HOME,
            onClick = { onItemSelected(EventlyNavItem.HOME) },
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Beranda") },
            label = { Text("Beranda") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            selected = selectedItem == EventlyNavItem.EXPLORE,
            onClick = { onItemSelected(EventlyNavItem.EXPLORE) },
            icon = { Icon(Icons.Rounded.Explore, contentDescription = "Jelajah") },
            label = { Text("Jelajah") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            selected = selectedItem == EventlyNavItem.TICKET,
            onClick = { onItemSelected(EventlyNavItem.TICKET) },
            icon = { Icon(Icons.Rounded.ConfirmationNumber, contentDescription = "Tiket") },
            label = { Text("Tiket") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            selected = selectedItem == EventlyNavItem.ANNOUNCEMENT,
            onClick = { onItemSelected(EventlyNavItem.ANNOUNCEMENT) },
            icon = { Icon(Icons.Rounded.Campaign, contentDescription = "Pengumuman") },
            label = { Text("Pengumuman") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                indicatorColor = Color.White
            )
        )
        NavigationBarItem(
            selected = selectedItem == EventlyNavItem.PROFILE,
            onClick = { onItemSelected(EventlyNavItem.PROFILE) },
            icon = { Icon(Icons.Rounded.Person, contentDescription = "Profil") },
            label = { Text("Profil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                indicatorColor = Color.White
            )
        )
    }
}
