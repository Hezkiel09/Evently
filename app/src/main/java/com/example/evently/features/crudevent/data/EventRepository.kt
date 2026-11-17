package com.example.evently.features.crudevent.data

import com.example.evently.core.network.SupabaseNetwork
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EventRepository private constructor() {
    private val client = SupabaseNetwork.client.from("events")

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    init {
        fetchEvents()
    }

    suspend fun getEvents(): List<Event> {
        return try {
            val rawList = client.select {
                order("id", Order.DESCENDING)
            }.decodeList<Event>()

            rawList.map { event ->
                val parts = event.eventDate.split("T")
                val parsedDate = parts.getOrNull(0) ?: "21 Nov"
                val parsedTime = parts.getOrNull(1)?.substringBefore("Z")?.take(5) ?: "18.30 WIB"
                event.copy(
                    date = if (event.date.isBlank()) parsedDate else event.date,
                    time = if (event.time.isBlank()) parsedTime else event.time
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun fetchEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            val result = getEvents()
            _events.value = result
            println("DEBUG REPOSITORY: fetched ${result.size} events from Supabase")
        }
    }

    suspend fun addEvent(event: Event) {
        try {
            val dateTime = if (event.date.isNotBlank() && event.time.isNotBlank()) {
                "${event.date}T${event.time}:00Z"
            } else if (event.eventDate.isNotBlank()) {
                event.eventDate
            } else {
                "2026-11-21T18:30:00Z"
            }

            val payload = buildJsonObject {
                put("title", event.name)
                put("description", event.description)
                put("banner_url", event.poster ?: "")
                put("location", event.location)
                put("event_date", dateTime)
                put("is_finished", event.isFinished)
                if (event.organizerId != null) {
                    put("organizer_id", event.organizerId)
                }
            }

            client.insert(payload)
            fetchEvents()
            println("DEBUG REPOSITORY: event added to Supabase")
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG REPOSITORY ERROR adding event: ${e.message}")
        }
    }

    suspend fun updateEvent(event: Event) {
        try {
            if (event.id != null) {
                val dateTime = if (event.date.isNotBlank() && event.time.isNotBlank()) {
                    "${event.date}T${event.time}:00Z"
                } else if (event.eventDate.isNotBlank()) {
                    event.eventDate
                } else {
                    "2026-11-21T18:30:00Z"
                }

                val payload = buildJsonObject {
                    put("title", event.name)
                    put("description", event.description)
                    put("banner_url", event.poster ?: "")
                    put("location", event.location)
                    put("event_date", dateTime)
                    put("is_finished", event.isFinished)
                    if (event.organizerId != null) {
                        put("organizer_id", event.organizerId)
                    }
                }

                client.update(payload) {
                    filter {
                        eq("id", event.id)
                    }
                }
                fetchEvents()
                println("DEBUG REPOSITORY: event updated in Supabase")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG REPOSITORY ERROR updating event: ${e.message}")
        }
    }

    suspend fun deleteEvent(eventId: Long) {
        try {
            client.delete {
                filter {
                    eq("id", eventId)
                }
            }
            fetchEvents()
            println("DEBUG REPOSITORY: event deleted from Supabase")
        } catch (e: Exception) {
            e.printStackTrace()
            println("DEBUG REPOSITORY ERROR deleting event: ${e.message}")
        }
    }

    companion object {
        val instance = EventRepository()
    }
}
