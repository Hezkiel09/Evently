package com.example.evently.features.crudevent.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.evently.features.crudevent.data.Event
import androidx.lifecycle.ViewModel
import com.example.evently.features.crudevent.data.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddEditEventUiState(
    val event: Event = Event(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false
)

class AddEditViewModel : ViewModel() {

    private val repository = EventRepository.instance

    private val _uiState = MutableStateFlow(
        AddEditEventUiState()
    )

    val uiState: StateFlow<AddEditEventUiState> =
        _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(name = name)
            )
        }
    }

    fun updateCategory(category: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(category = category)
            )
        }
    }

    fun updateDate(date: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(date = date)
            )
        }
    }

    fun updateTime(time: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(time = time)
            )
        }
    }

    fun updateLocation(location: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(location = location)
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(description = description)
            )
        }
    }

    fun updatePoster(poster: String) {
        _uiState.update {
            it.copy(
                event = it.event.copy(poster = poster)
            )
        }
    }

    fun saveEvent() {
        val event = _uiState.value.event

        println("DEBUG SAVE: $event")

        viewModelScope.launch {
            println("DEBUG REPOSITORY: adding event")
            repository.addEvent(event)
            println("DEBUG REPOSITORY: event added")
        }
    }
}