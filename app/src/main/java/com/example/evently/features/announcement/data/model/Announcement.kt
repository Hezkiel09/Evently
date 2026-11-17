package com.example.evently.features.announcement.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnnouncementDto(
    @SerialName("id") val id: Long = 0,
    @SerialName("event_id") val eventId: Long,
    @SerialName("author_id") val authorId: String? = null,
    @SerialName("title") val title: String,
    @SerialName("content") val content: String,
    @SerialName("is_urgent") val isUrgent: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("events") val event: EventDto? = null
)

@Serializable
data class EventDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("location") val location: String = "",
    @SerialName("organizer_id") val organizerId: String? = null
)

// Data Class domain UI milikmu
data class Announcement(
    val id: Long,
    val eventId: Long,
    val title: String,
    val event: String,
    val location: String,
    val time: String,
    val date: String,
    val preview: String,
    val content: String,
    val urgent: Boolean
)