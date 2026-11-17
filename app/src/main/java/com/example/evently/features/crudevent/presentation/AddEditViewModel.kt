package com.example.evently.features.crudevent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.core.network.SupabaseNetwork
import com.example.evently.features.crudevent.data.Event
import com.example.evently.features.crudevent.data.EventRepository
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditEventUiState(
    val event: Event = Event(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class AddEditViewModel : ViewModel() {

    private val repository = EventRepository.instance

    private val _uiState = MutableStateFlow(AddEditEventUiState())
    val uiState: StateFlow<AddEditEventUiState> = _uiState.asStateFlow()

    fun loadEvent(event: Event) {
        _uiState.update {
            it.copy(
                event = event,
                isEditMode = true,
                successMessage = null,
                errorMessage = null
            )
        }
    }

    fun resetForm() {
        _uiState.value = AddEditEventUiState()
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(event = it.event.copy(name = name)) }
    }

    fun updateCategory(category: String) {
        _uiState.update { it.copy(event = it.event.copy(category = category)) }
    }

    fun updateDate(date: String) {
        _uiState.update { it.copy(event = it.event.copy(date = date)) }
    }

    fun updateTime(time: String) {
        _uiState.update { it.copy(event = it.event.copy(time = time)) }
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(event = it.event.copy(location = location)) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(event = it.event.copy(description = description)) }
    }

    fun updatePoster(poster: String) {
        _uiState.update { it.copy(event = it.event.copy(poster = poster)) }
    }

    fun saveEvent() {
        val event = _uiState.value.event
        val isEdit = _uiState.value.isEditMode

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val userId = try {
                    SupabaseNetwork.client.auth.currentUserOrNull()?.id
                } catch (_: Exception) {
                    null
                }

                if (userId.isNullOrBlank()) {
                    throw IllegalStateException("Silakan login terlebih dahulu untuk membuat event.")
                }

                val eventWithOrganizer = event.copy(organizerId = userId)

                if (isEdit && event.id != null) {
                    repository.updateEvent(eventWithOrganizer)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            successMessage = "Event berhasil diperbarui"
                        )
                    }
                } else {
                    repository.addEvent(eventWithOrganizer)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            successMessage = "Event berhasil disimpan"
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val msg = e.localizedMessage ?: e.message ?: "Terjadi kesalahan"
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Gagal menyimpan event: $msg"
                    )
                }
            }
        }
    }
}
