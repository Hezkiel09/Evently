package com.example.evently.features.crudevent.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventRepository {

    private val _events = MutableStateFlow<List<Event>>(
        listOf(
            Event(
                id = 1,
                name = "Sunset Music Session",
                category = "Music",
                date = "21 Nov",
                time = "18.30 WIB",
                location = "Taman Menteng, Jakarta",
                description = "Music session bersama Evently"
            ),
            Event(
                id = 2,
                name = "Creative Meetup Jakarta",
                category = "Education",
                date = "28 Nov",
                time = "10.00 WIB",
                location = "M Bloc Space, Jakarta",
                description = "Creative meetup untuk komunitas"
            )
        )
    )

    val events: StateFlow<List<Event>> =
        _events.asStateFlow()

    suspend fun addEvent(event: Event) {
        delay(300)

        _events.update { currentEvents ->
            currentEvents + event.copy(
                id = (currentEvents.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
            )
        }
    }
    companion object {
        val instance = EventRepository()
    }
}