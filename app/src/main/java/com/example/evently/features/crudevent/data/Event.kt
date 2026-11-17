package com.example.evently.features.crudevent.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id") val id: Long? = null,
    @SerialName("organizer_id") val organizerId: String? = null,
    @SerialName("banner_url") val poster: String? = null,
    @SerialName("title") val name: String = "",
    val category: String = "Music",
    @SerialName("event_date") val eventDate: String = "",
    val date: String = "",
    val time: String = "",
    @SerialName("location") val location: String = "",
    @SerialName("description") val description: String = "",
    @SerialName("is_finished") val isFinished: Boolean = false
)
