package com.example.evently.features.home.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class EventUiModel(
    val id: Int,
    val title: String,
    val category: String,
    val date: String,
    val time: String,
    val location: String,
    val organizer: String
)

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "Semua",
    val categories: List<String> = listOf(
        "Semua",
        "Music",
        "Education",
        "Film"
    ),
    val events: List<EventUiModel> = emptyList()
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            events = dummyEvents
        )
    )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = query
            )
        }
    }

    fun selectCategory(category: String) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category
            )
        }
    }

    fun getFilteredEvents(): List<EventUiModel> {
        val state = _uiState.value

        return state.events.filter { event ->

            val matchesSearch =
                event.title.contains(
                    state.searchQuery,
                    ignoreCase = true
                )

            val matchesCategory =
                state.selectedCategory == "Semua" ||
                        event.category == state.selectedCategory

            matchesSearch && matchesCategory
        }
    }

    companion object {

        private val dummyEvents = listOf(

            EventUiModel(
                id = 1,
                title = "Sunset Music Session",
                category = "Music",
                date = "21 Nov",
                time = "18.30 WIB",
                location = "Taman Menteng, Jakarta",
                organizer = "Evently"
            ),

            EventUiModel(
                id = 2,
                title = "Creative Meetup Jakarta",
                category = "Education",
                date = "28 Nov",
                time = "10.00 WIB",
                location = "M Bloc Space, Jakarta",
                organizer = "Evently"
            )
        )
    }
}