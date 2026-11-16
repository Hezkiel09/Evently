package com.example.evently.features.announcement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.features.announcement.data.model.Announcement
import com.example.evently.features.announcement.data.model.EventDto
import com.example.evently.features.announcement.data.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnnouncementViewModel : ViewModel() {
    private val repository = AnnouncementRepository()

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements.asStateFlow()

    private val _events = MutableStateFlow<List<EventDto>>(emptyList())
    val events: StateFlow<List<EventDto>> = _events.asStateFlow()

    private val _selectedAnnouncement = MutableStateFlow<Announcement?>(null)
    val selectedAnnouncement: StateFlow<Announcement?> = _selectedAnnouncement.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Fetch Events & Announcements concurrently
                val eventList = repository.getEvents()
                _events.value = eventList

                val dtoList = repository.getAnnouncements()
                _announcements.value = dtoList.map { dto ->
                    Announcement(
                        id = dto.id,
                        eventId = dto.eventId,
                        title = dto.title,
                        event = dto.event?.title ?: "Event Kampus",
                        location = dto.event?.location ?: "Lokasi Event",
                        time = "Baru saja",
                        date = dto.createdAt?.take(10) ?: "Hari ini",
                        preview = dto.content,
                        content = dto.content,
                        urgent = dto.isUrgent
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnnouncement(announcement: Announcement?) {
        _selectedAnnouncement.value = announcement
    }

    fun saveAnnouncement(title: String, eventId: Long, content: String, editingId: Long?) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                if (editingId != null) {
                    repository.updateAnnouncement(editingId, title, eventId, content)
                } else {
                    repository.createAnnouncement(title, eventId, content)
                }
                _selectedAnnouncement.value = null
                loadData() // Refresh list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAnnouncement(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.deleteAnnouncement(id)
                loadData() // Refresh list
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}