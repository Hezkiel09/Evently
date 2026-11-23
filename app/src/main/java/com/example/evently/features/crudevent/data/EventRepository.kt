package com.example.evently.features.crudevent.data

import android.util.Log
import com.example.evently.core.network.SupabaseNetwork
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class EventRepository private constructor() {
    private val table = SupabaseNetwork.client.from("events")

    suspend fun getEvents(): List<Event> = table.select {
        order("event_date", Order.ASCENDING)
    }.decodeList()

    suspend fun addEvent(event: Event) {
        val currentUserId = SupabaseNetwork.client.auth.currentUserOrNull()?.id
        if (currentUserId.isNullOrBlank()) {
            throw IllegalStateException("User belum login.")
        }
        Log.d("EVENT_INSERT", "authUserId=$currentUserId")
        Log.d("EVENT_INSERT", "eventOrganizerId=${event.organizerId}")
        Log.d("EVENT_INSERT", "payloadOrganizerId=$currentUserId")
        table.insert(payload(event, currentUserId))
    }

    suspend fun updateEvent(event: Event, organizerId: String) {
        val id = event.id ?: throw IllegalArgumentException("ID event tidak ditemukan.")
        val payload = payload(event, organizerId)
        // Do not allow a client update to transfer ownership.
        table.update(payload) {
            filter {
                eq("id", id)
                eq("organizer_id", organizerId)
            }
        }
    }

    suspend fun deleteEvent(eventId: Long, organizerId: String) {
        table.delete {
            filter {
                eq("id", eventId)
                eq("organizer_id", organizerId)
            }
        }
    }

    private fun payload(event: Event, organizerId: String) = buildJsonObject {
        put("organizer_id", organizerId)
        put("title", event.name.trim())
        put("description", event.description.trim())
        put("banner_url", event.bannerUrl?.trim()?.takeIf(String::isNotEmpty))
        put("location", event.location.trim())
        put("event_date", event.eventDate)
        put("is_finished", event.isFinished)
    }

    companion object {
        val instance = EventRepository()
    }
}
