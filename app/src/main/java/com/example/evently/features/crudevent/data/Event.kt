package com.example.evently.features.crudevent.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id") val id: Long? = null,
    @SerialName("organizer_id") val organizerId: String? = null,
    @SerialName("title") val name: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("banner_url") val bannerUrl: String? = null,
    @SerialName("location") val location: String = "",
    @SerialName("event_date") val eventDate: String = "",
    @SerialName("is_finished") val isFinished: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null
)
