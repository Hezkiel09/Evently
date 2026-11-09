package com.example.evently.features.crudevent.data

data class Event(
    val id: Int? = null,
    val poster: String? = null,
    val name: String = "",
    val category: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = ""
)