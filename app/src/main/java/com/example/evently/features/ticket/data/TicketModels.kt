package com.example.evently.features.ticket.data

enum class TicketStatus(val label: String) {
    UPCOMING("SEGERA"),
    COMPLETED("SELESAI"),
    CANCELLED("DIBATALKAN")
}

data class EventTicketItem(
    val id: String,
    val title: String,
    val category: String,
    val dateGroup: String,
    val timeRange: String,
    val location: String,
    val isToday: Boolean,
    val quotaRemaining: Int,
    val priceLabel: String = "Gratis"
)

data class UserTicket(
    val ticketCode: String,
    val eventId: String,
    val eventTitle: String,
    val category: String,
    val date: String,
    val timeRange: String,
    val location: String,
    val holderName: String,
    val status: TicketStatus
)

object TicketDummyData {
    val sampleEvents = listOf(
        EventTicketItem(
            id = "EVT-01",
            title = "Schotival 2026: Tech & Career Expo",
            category = "WORKSHOP",
            dateGroup = "17 November 2026",
            timeRange = "13:30 — 14:30 WIB",
            location = "Fakultas Ilmu Komputer, Universitas Brawijaya",
            isToday = true,
            quotaRemaining = 24,
            priceLabel = "Gratis"
        ),
        EventTicketItem(
            id = "EVT-02",
            title = "UI/UX Design System Bootcamp",
            category = "SEMINAR",
            dateGroup = "17 November 2026",
            timeRange = "15:30 — 17:00 WIB",
            location = "Auditorium Algoritma, FILKOM UB",
            isToday = true,
            quotaRemaining = 12,
            priceLabel = "Gratis"
        ),
        EventTicketItem(
            id = "EVT-03",
            title = "National Campus Leadership Summit",
            category = "KONFERENSI",
            dateGroup = "19 November 2026",
            timeRange = "09:00 — 12:00 WIB",
            location = "Gedung Samantha Krida, Universitas Brawijaya",
            isToday = false,
            quotaRemaining = 50,
            priceLabel = "Gratis"
        ),
        EventTicketItem(
            id = "EVT-04",
            title = "Android Kotlin & Compose Hackday",
            category = "WORKSHOP",
            dateGroup = "19 November 2026",
            timeRange = "13:00 — 16:00 WIB",
            location = "Fakultas Ilmu Komputer, Universitas Brawijaya",
            isToday = false,
            quotaRemaining = 8,
            priceLabel = "Gratis"
        )
    )

    val initialUserTickets = listOf(
        UserTicket(
            ticketCode = "EVT-2026-8841",
            eventId = "EVT-OLD-01",
            eventTitle = "Festival Seni Kampus 2026",
            category = "FESTIVAL",
            date = "10 November 2026",
            timeRange = "18:30 — 21:00 WIB",
            location = "Fakultas Ilmu Komputer, UB",
            holderName = "Mahasiswa Evently",
            status = TicketStatus.COMPLETED
        )
    )
}