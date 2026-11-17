package com.example.evently.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.core.auth.AuthRepository
import com.example.evently.features.crudevent.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventUiModel(
    val id: Long,
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
        "Film",
        "Workshop",
        "Seminar"
    ),
    val events: List<EventUiModel> = emptyList(),
    val userRole: String = "student"
)

class HomeViewModel : ViewModel() {

    private val repository =
        EventRepository.instance
    private val authRepository = AuthRepository()

    private val _uiState =
        MutableStateFlow(
            HomeUiState()
        )

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    init {
        observeEvents()
        loadUserRole()
    }

    private fun loadUserRole() {
        viewModelScope.launch {
            val profile = authRepository.getCurrentProfile()
            _uiState.update {
                it.copy(userRole = profile?.role ?: "student")
            }
        }
    }

    private fun observeEvents() {

        viewModelScope.launch {

            repository.events.collect { events ->

                val eventUiModels =
                    events.map { event ->

                        EventUiModel(
                            id = event.id ?: 0L,
                            title = event.name,
                            category = event.category,
                            date = event.date,
                            time = event.time,
                            location = event.location,
                            organizer = "Evently"
                        )
                    }

                _uiState.update {
                    it.copy(
                        events = eventUiModels
                    )
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {

        _uiState.update {
            it.copy(
                searchQuery = query
            )
        }
    }

    fun selectCategory(category: String) {

        _uiState.update {
            it.copy(
                selectedCategory = category
            )
        }
    }

    fun deleteEvent(eventId: Long) {
        viewModelScope.launch {
            repository.deleteEvent(eventId)
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
}
