package com.example.evently.features.announcement.data.repository

import com.example.evently.core.network.SupabaseNetwork
import com.example.evently.features.announcement.data.model.AnnouncementDto
import com.example.evently.features.announcement.data.model.EventDto
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AnnouncementRepository {
    private val client = SupabaseNetwork.client.from("announcements")
    private val eventClient = SupabaseNetwork.client.from("events")

    // READ: Get All Announcements with Joined Event Data
    suspend fun getAnnouncements(): List<AnnouncementDto> {
        return client.select(columns = Columns.raw("*, events(id, title, location)")) {
            order("created_at", Order.DESCENDING)
        }.decodeList<AnnouncementDto>()
    }

    // READ: Get All Events (Untuk Dropdown Form)
    suspend fun getEvents(): List<EventDto> {
        return eventClient.select().decodeList<EventDto>()
    }

    // INSERT: Create New Announcement
    suspend fun createAnnouncement(title: String, eventId: Long, content: String) {
        val newRecord = buildJsonObject {
            put("title", title)
            put("event_id", eventId)
            put("content", content)
            put("is_urgent", true)
        }
        client.insert(newRecord)
    }

    // UPDATE: Edit Announcement
    suspend fun updateAnnouncement(id: Long, title: String, eventId: Long, content: String) {
        val updateRecord = buildJsonObject {
            put("title", title)
            put("event_id", eventId)
            put("content", content)
        }
        client.update(updateRecord) {
            filter {
                eq("id", id)
            }
        }
    }

    // DELETE: Remove Announcement
    suspend fun deleteAnnouncement(id: Long) {
        client.delete {
            filter {
                eq("id", id)
            }
        }
    }
}