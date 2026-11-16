package com.example.evently.features.ticket.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.evently.features.ticket.data.EventTicketItem
import com.example.evently.features.ticket.data.TicketDummyData
import com.example.evently.features.ticket.data.TicketStatus
import com.example.evently.features.ticket.data.UserTicket
import kotlin.random.Random

enum class EventFilterTab(val title: String) {
    ALL("Semua"),
    TODAY("Hari Ini")
}

class TicketViewModel : ViewModel() {

    private val _events = mutableStateListOf<EventTicketItem>().apply {
        addAll(TicketDummyData.sampleEvents)
    }

    private val _myTickets = mutableStateListOf<UserTicket>().apply {
        addAll(TicketDummyData.initialUserTickets)
    }
    val myTickets: List<UserTicket> get() = _myTickets

    var selectedLocation by mutableStateOf("Fakultas Ilmu Komputer, Universitas Brawijaya")
        private set

    var selectedFilter by mutableStateOf(EventFilterTab.ALL)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isSearchVisible by mutableStateOf(false)
        private set

    var eventToRegister by mutableStateOf<EventTicketItem?>(null)
        private set

    var selectedTicketForQr by mutableStateOf<UserTicket?>(null)
        private set

    var snackbarMessage by mutableStateOf<String?>(null)
        private set

    val filteredEvents: List<EventTicketItem>
        get() = _events.filter { event ->
            val matchesTab = when (selectedFilter) {
                EventFilterTab.ALL -> true
                EventFilterTab.TODAY -> event.isToday
            }
            val matchesQuery = searchQuery.isBlank() ||
                    event.title.contains(searchQuery, ignoreCase = true) ||
                    event.location.contains(searchQuery, ignoreCase = true) ||
                    event.category.contains(searchQuery, ignoreCase = true)
            matchesTab && matchesQuery
        }

    val activeTicketCount: Int
        get() = _myTickets.count { it.status == TicketStatus.UPCOMING }

    fun updateFilter(tab: EventFilterTab) {
        selectedFilter = tab
    }

    fun toggleSearch() {
        isSearchVisible = !isSearchVisible
        if (!isSearchVisible) searchQuery = ""
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun isEventRegistered(eventId: String): Boolean {
        return _myTickets.any { it.eventId == eventId && it.status == TicketStatus.UPCOMING }
    }

    fun getTicketByEventId(eventId: String): UserTicket? {
        return _myTickets.find { it.eventId == eventId && it.status == TicketStatus.UPCOMING }
    }

    fun openRegistrationSheet(event: EventTicketItem) {
        val existingTicket = getTicketByEventId(event.id)
        if (existingTicket != null) {
            selectedTicketForQr = existingTicket
        } else {
            eventToRegister = event
        }
    }

    fun dismissRegistrationSheet() {
        eventToRegister = null
    }

    fun confirmRegistration(event: EventTicketItem, attendeeName: String = "Peserta Evently") {
        if (isEventRegistered(event.id)) {
            eventToRegister = null
            return
        }

        val randomCode = Random.nextInt(1000, 9999)
        val newTicket = UserTicket(
            ticketCode = "EVT-2026-$randomCode",
            eventId = event.id,
            eventTitle = event.title,
            category = event.category,
            date = event.dateGroup,
            timeRange = event.timeRange,
            location = event.location,
            holderName = attendeeName.ifBlank { "Peserta Evently" },
            status = TicketStatus.UPCOMING
        )

        _myTickets.add(0, newTicket)

        val index = _events.indexOfFirst { it.id == event.id }
        if (index != -1 && _events[index].quotaRemaining > 0) {
            _events[index] = _events[index].copy(
                quotaRemaining = _events[index].quotaRemaining - 1
            )
        }

        eventToRegister = null
        snackbarMessage = "Tiket berhasil diterbitkan! Cek di menu Tiket Saya."
    }

    fun cancelTicket(ticketCode: String) {
        val index = _myTickets.indexOfFirst { it.ticketCode == ticketCode }
        if (index != -1) {
            val ticket = _myTickets[index]
            _myTickets[index] = ticket.copy(status = TicketStatus.CANCELLED)

            val eventIndex = _events.indexOfFirst { it.id == ticket.eventId }
            if (eventIndex != -1) {
                _events[eventIndex] = _events[eventIndex].copy(
                    quotaRemaining = _events[eventIndex].quotaRemaining + 1
                )
            }
            selectedTicketForQr = null
            snackbarMessage = "Pendaftaran tiket $ticketCode dibatalkan."
        }
    }

    fun openTicketQr(ticket: UserTicket) {
        selectedTicketForQr = ticket
    }

    fun dismissTicketQr() {
        selectedTicketForQr = null
    }

    fun clearSnackbar() {
        snackbarMessage = null
    }
}