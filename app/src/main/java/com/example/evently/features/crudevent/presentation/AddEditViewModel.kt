package com.example.evently.features.crudevent.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.core.auth.AuthRepository
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
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(AddEditEventUiState())
    val uiState: StateFlow<AddEditEventUiState> = _uiState.asStateFlow()

    fun loadEvent(event: Event) = _uiState.update {
        it.copy(event = event, isEditMode = true, successMessage = null, errorMessage = null)
    }
    fun resetForm() { _uiState.value = AddEditEventUiState() }
    fun updateName(value: String) = _uiState.update { it.copy(event = it.event.copy(name = value)) }
    fun updateLocation(value: String) = _uiState.update { it.copy(event = it.event.copy(location = value)) }
    fun updateDescription(value: String) = _uiState.update { it.copy(event = it.event.copy(description = value)) }
    fun updatePoster(value: String) = _uiState.update { it.copy(event = it.event.copy(bannerUrl = value)) }
    fun updateDate(date: String) {
        val time = _uiState.value.event.eventDate.substringAfter('T', "18:30:00Z")
        _uiState.update { it.copy(event = it.event.copy(eventDate = "${date}T$time")) }
    }
    fun updateTime(time: String) {
        val date = _uiState.value.event.eventDate.substringBefore('T', "")
        _uiState.update { it.copy(event = it.event.copy(eventDate = "${date}T${time}:00Z")) }
    }

    fun saveEvent() {
        if (_uiState.value.isLoading) return
        val event = _uiState.value.event
        val edit = _uiState.value.isEditMode
        val date = event.eventDate.substringBefore('T')
        if (event.name.isBlank() || event.location.isBlank() || date.isBlank() || event.description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Nama event, lokasi, tanggal, dan deskripsi wajib diisi.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
            try {
                val user = SupabaseNetwork.client.auth.currentUserOrNull()
                    ?: throw IllegalStateException("Silakan login terlebih dahulu.")
                val profile = authRepository.getCurrentProfile()
                if (profile?.role?.lowercase() !in setOf("organizer", "panitia")) {
                    throw IllegalStateException("Hanya organizer yang dapat mengelola event.")
                }
                if (edit && event.organizerId != user.id) {
                    throw IllegalStateException("Anda hanya dapat mengedit event milik sendiri.")
                }
                if (edit) {
                    repository.updateEvent(event, user.id)
                } else {
                    repository.addEvent(event)
                }
                _uiState.update { it.copy(isLoading = false, isSuccess = true, successMessage = if (edit) "Event berhasil diperbarui" else "Event berhasil disimpan") }
            } catch (e: Exception) {
                val detail = e.message.orEmpty().lowercase()
                val safeMessage = when {
                    "row-level security" in detail || "violates row-level" in detail ->
                        "Supabase menolak penyimpanan event (RLS). Organizer ID dari sesi sudah dikirim; periksa kondisi policy INSERT."
                    "user belum login" in detail || "silakan login" in detail ->
                        "User belum login. Silakan login kembali."
                    else -> "Gagal menyimpan event. Periksa data dan koneksi, lalu coba lagi."
                }
                _uiState.update { it.copy(isLoading = false, errorMessage = safeMessage) }
            }
        }
    }
}
