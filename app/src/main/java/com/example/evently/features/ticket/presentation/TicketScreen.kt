package com.example.evently.features.ticket.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.evently.features.ticket.data.TicketStatus
import com.example.evently.features.ticket.presentation.components.*

@Composable
fun TicketFeatureRoute(
    viewModel: TicketViewModel = remember { TicketViewModel() }
) {
    var showMyTicketsPage by remember { mutableStateOf(false) }

    if (showMyTicketsPage) {
        MyTicketsScreen(
            viewModel = viewModel,
            onNavigateBack = { showMyTicketsPage = false }
        )
    } else {
        TicketCatalogScreen(
            viewModel = viewModel,
            onOpenMyTickets = { showMyTicketsPage = true }
        )
    }
}

@Composable
fun TicketCatalogScreen(
    viewModel: TicketViewModel,
    onOpenMyTickets: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val groupedEvents = viewModel.filteredEvents.groupBy { it.dateGroup }

    LaunchedEffect(viewModel.snackbarMessage) {
        viewModel.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tiket Event",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EventlyDarkText
                    )

                    Button(
                        onClick = onOpenMyTickets,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = EventlyOrange),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = if (viewModel.activeTicketCount > 0) {
                                "Tiket Saya (${viewModel.activeTicketCount})"
                            } else {
                                "Tiket Saya"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EventlyOrangeLight,
                    border = BorderStroke(1.dp, EventlyOrange.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        EventlyLocationIcon(
                            tint = EventlyOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = viewModel.selectedLocation,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = EventlyDarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = EventlySurfaceGray,
                            modifier = Modifier.height(40.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                EventFilterTab.entries.forEach { tab ->
                                    val isSelected = viewModel.selectedFilter == tab
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (isSelected) Color.White else Color.Transparent,
                                        shadowElevation = if (isSelected) 1.dp else 0.dp,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .clickable { viewModel.updateFilter(tab) }
                                    ) {
                                        Text(
                                            text = tab.title,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) EventlyDarkText else EventlyMutedText,
                                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = if (viewModel.isSearchVisible) EventlyOrangeLight else EventlySurfaceGray,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable { viewModel.toggleSearch() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (viewModel.isSearchVisible) {
                                    EventlyCloseIcon(
                                        tint = EventlyOrange,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    EventlySearchIcon(
                                        tint = EventlyDarkText,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }

                    AnimatedVisibility(visible = viewModel.isSearchVisible) {
                        OutlinedTextField(
                            value = viewModel.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = "Cari nama event atau kategori...",
                                    fontSize = 13.sp,
                                    color = EventlyMutedText
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = EventlyDarkText,
                                unfocusedTextColor = EventlyDarkText,
                                cursorColor = EventlyOrange,
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = EventlyOrange,
                                unfocusedBorderColor = EventlyBorderGray
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (groupedEvents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada event yang sesuai filter.",
                            fontSize = 14.sp,
                            color = EventlyMutedText
                        )
                    }
                }
            } else {
                groupedEvents.forEach { (dateHeader, eventsInDate) ->
                    item(key = dateHeader) {
                        Text(
                            text = dateHeader,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EventlyDarkText,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    items(eventsInDate, key = { it.id }) { event ->
                        EventCatalogCard(
                            event = event,
                            isRegistered = viewModel.isEventRegistered(event.id),
                            onActionClick = { viewModel.openRegistrationSheet(event) }
                        )
                    }
                }
            }
        }
    }

    viewModel.eventToRegister?.let { selectedEvent ->
        RegistrationConfirmationSheet(
            event = selectedEvent,
            onDismiss = { viewModel.dismissRegistrationSheet() },
            onConfirm = { name -> viewModel.confirmRegistration(selectedEvent, name) }
        )
    }

    viewModel.selectedTicketForQr?.let { ticket ->
        TicketQrDetailSheet(
            ticket = ticket,
            onDismiss = { viewModel.dismissTicketQr() },
            onCancelTicket = { code -> viewModel.cancelTicket(code) }
        )
    }
}

@Composable
fun MyTicketsScreen(
    viewModel: TicketViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tiket Aktif", "Riwayat")

    val displayedTickets = viewModel.myTickets.filter { ticket ->
        if (selectedTab == 0) {
            ticket.status == TicketStatus.UPCOMING
        } else {
            ticket.status != TicketStatus.UPCOMING
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA),
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        EventlyBackIcon(
                            tint = EventlyDarkText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Text(
                        text = "Tiket Saya",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EventlyDarkText,
                        modifier = Modifier.weight(1f)
                    )
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = EventlyOrange
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) EventlyOrange else EventlyMutedText
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        if (displayedTickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (selectedTab == 0) "Belum ada tiket aktif" else "Belum ada riwayat tiket",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EventlyMutedText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(displayedTickets, key = { it.ticketCode }) { ticket ->
                    DigitalTicketCard(
                        ticket = ticket,
                        onClick = { viewModel.openTicketQr(ticket) }
                    )
                }
            }
        }
    }

    viewModel.selectedTicketForQr?.let { ticket ->
        TicketQrDetailSheet(
            ticket = ticket,
            onDismiss = { viewModel.dismissTicketQr() },
            onCancelTicket = { code -> viewModel.cancelTicket(code) }
        )
    }
}