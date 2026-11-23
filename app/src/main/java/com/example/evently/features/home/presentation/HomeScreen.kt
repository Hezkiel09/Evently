package com.example.evently.features.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.evently.core.ui.components.EventlyNavigationBar
import com.example.evently.core.ui.components.EventlyNavItem
import com.example.evently.features.home.components.LocationHeader
import com.example.evently.features.home.components.SearchBar
import com.example.evently.features.home.components.UpComingEventSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onAddEventClick: () -> Unit = {},
    onEditEventClick: (EventUiModel) -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onNavItemClick: (EventlyNavItem) -> Unit = {},
    isLoggingOut: Boolean = false,
    authErrorMessage: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    var eventToDelete by remember { mutableStateOf<EventUiModel?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authErrorMessage) {
        authErrorMessage?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Evently") },
                actions = {
                    IconButton(onClick = onLogoutClick, enabled = !isLoggingOut) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFF3B1C5A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            EventlyNavigationBar(
                selectedItem = EventlyNavItem.HOME,
                onItemSelected = onNavItemClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            LocationHeader()

            uiState.profileErrorMessage?.let { message ->
                Text(message, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                TextButton(onClick = { viewModel.loadUserRole() }) {
                    Text("Coba muat profil lagi")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(
                value = uiState.searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(24.dp))
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.errorMessage != null -> Column {
                    Text("Gagal memuat event: ${uiState.errorMessage}", color = Color.Red)
                    TextButton(onClick = viewModel::refreshEvents) { Text("Coba lagi") }
                }
                else -> UpComingEventSection(
                    events = viewModel.getFilteredEvents(),
                    userRole = uiState.userRole,
                    onAddEventClick = onAddEventClick,
                    onEditEventClick = onEditEventClick,
                    onDeleteEventClick = { event -> eventToDelete = event }
                )
            }
        }

        eventToDelete?.let { event ->
            AlertDialog(
                onDismissRequest = { eventToDelete = null },
                title = { Text("Hapus Event") },
                text = { Text("Apakah Anda yakin ingin menghapus event \"${event.title}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteEvent(event.id)
                            eventToDelete = null
                        }
                    ) {
                        Text("Hapus", color = Color.Red)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { eventToDelete = null }
                    ) {
                        Text("Batal")
                    }
                }
            )
        }
    }
}
