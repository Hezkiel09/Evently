package com.example.evently.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.core.auth.AuthRepository
import com.example.evently.core.network.SupabaseNetwork
import com.example.evently.features.crudevent.data.EventRepository
import com.example.evently.features.crudevent.data.Event
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class EventUiModel(
    val id: Long,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val organizer: String,
    val bannerUrl: String?,
    val organizerId: String?,
    val canManage: Boolean
)

data class HomeUiState(
    val searchQuery: String = "",
    val events: List<EventUiModel> = emptyList(),
    val userRole: String = "student",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val profileErrorMessage: String? = null
)

class HomeViewModel : ViewModel() {
    private val repository = EventRepository.instance
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(HomeUiState())
    private var domainEvents: List<Event> = emptyList()
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init { refreshEvents(); loadUserRole() }

    fun loadUserRole() {
        _uiState.update { it.copy(userRole = "student", profileErrorMessage = null) }
        viewModelScope.launch {
            try {
                val profile = authRepository.getCurrentProfile()
                    ?: throw IllegalStateException("Profil pengguna belum tersedia.")
                val role = when (profile.role.lowercase()) {
                    "panitia", "organizer" -> "organizer"
                    "admin" -> "admin"
                    "mahasiswa", "student" -> "student"
                    else -> throw IllegalStateException("Role profil tidak valid.")
                }
                _uiState.update { it.copy(userRole = role, profileErrorMessage = null) }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        userRole = "student",
                        profileErrorMessage = "Profil pengguna belum tersedia. Silakan coba lagi atau hubungi administrator."
                    )
                }
            }
            refreshEvents()
        }
    }

    fun refreshEvents() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            val userId = SupabaseNetwork.client.auth.currentUserOrNull()?.id
            val records = repository.getEvents()
            domainEvents = records
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", java.util.Locale.getDefault())
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", java.util.Locale.getDefault())
            val mapped = records.mapNotNull { event ->
                val id = event.id ?: return@mapNotNull null
                val parsed = runCatching { OffsetDateTime.parse(event.eventDate) }.getOrNull()
                EventUiModel(id, event.name,
                    parsed?.format(formatter) ?: event.eventDate,
                    parsed?.format(timeFormatter) ?: "",
                    event.location, "Evently", event.bannerUrl,
                    event.organizerId,
                    _uiState.value.userRole == "admin" ||
                        (_uiState.value.userRole == "organizer" && userId != null && event.organizerId == userId))
            }
            _uiState.update { it.copy(events = mapped, isLoading = false) }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Gagal memuat event.") }
        }
    }

    fun updateSearchQuery(query: String) = _uiState.update { it.copy(searchQuery = query) }
    fun findEvent(id: Long): Event? = domainEvents.find { it.id == id }

    fun deleteEvent(eventId: Long) = viewModelScope.launch {
        val userId = SupabaseNetwork.client.auth.currentUserOrNull()?.id
        if (userId.isNullOrBlank() || _uiState.value.userRole !in setOf("organizer", "admin")) {
            _uiState.update { it.copy(errorMessage = "Akun ini tidak memiliki izin untuk menghapus event.") }
            return@launch
        }
        val event = _uiState.value.events.find { it.id == eventId }
        if (event?.canManage != true || event.organizerId.isNullOrBlank()) {
            _uiState.update { it.copy(errorMessage = "Anda hanya dapat menghapus event milik sendiri.") }
            return@launch
        }
        try {
            repository.deleteEvent(eventId, event.organizerId)
            refreshEvents()
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = e.localizedMessage ?: "Gagal menghapus event.") }
        }
    }

    fun getFilteredEvents(): List<EventUiModel> = _uiState.value.events.filter {
        it.title.contains(_uiState.value.searchQuery, ignoreCase = true)
    }
}
