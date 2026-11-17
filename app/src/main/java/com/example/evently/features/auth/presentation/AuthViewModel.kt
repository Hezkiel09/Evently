package com.example.evently.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.evently.core.auth.AuthRepository
import com.example.evently.core.auth.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val selectedRole: String = "student", // "student" or "organizer" (admin never selectable via UI)
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val isAuthenticated: Boolean = false,
    val userProfile: UserProfile? = null
)

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        checkSession()
    }

    fun checkSession() {
        viewModelScope.launch {
            if (authRepository.isUserLoggedIn()) {
                val profile = authRepository.getCurrentProfile()
                _uiState.update {
                    it.copy(
                        isAuthenticated = true,
                        userProfile = profile
                    )
                }
            } else {
                _uiState.update {
                    it.copy(isAuthenticated = false, userProfile = null)
                }
            }
        }
    }

    fun updateEmail(email: String) { _uiState.update { it.copy(email = email, errorMessage = null) } }
    fun updatePassword(password: String) { _uiState.update { it.copy(password = password, errorMessage = null) } }
    fun updateConfirmPassword(confirm: String) { _uiState.update { it.copy(confirmPassword = confirm, errorMessage = null) } }
    fun updateFullName(name: String) { _uiState.update { it.copy(fullName = name, errorMessage = null) } }
    
    fun selectRole(role: String) {
        if (role == "admin") return // Constraint 1: admin never selectable via UI
        _uiState.update { it.copy(selectedRole = role) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun signIn(onSuccess: (role: String) -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email dan password tidak boleh kosong.") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val profile = authRepository.signIn(state.email.trim(), state.password)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isAuthenticated = true,
                        userProfile = profile,
                        successMessage = "Login berhasil!"
                    )
                }
                onSuccess(profile.role)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal login: ${e.localizedMessage ?: "Periksa kembali email dan password."}"
                    )
                }
            }
        }
    }

    fun validateSignUp(onRoleSelectionNeeded: () -> Unit) {
        val state = _uiState.value
        if (state.fullName.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Semua field harus diisi.") }
            return
        }
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Konfirmasi password tidak cocok.") }
            return
        }
        if (state.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password minimal 6 karakter.") }
            return
        }
        onRoleSelectionNeeded()
    }

    fun finalizeSignUp(onSuccessToLogin: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val role = if (state.selectedRole == "organizer") "organizer" else "student"
                authRepository.signUp(state.email.trim(), state.password, state.fullName.trim(), role)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Registrasi berhasil! Silakan login."
                    )
                }
                onSuccessToLogin()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Gagal mendaftar: ${e.localizedMessage ?: "Terjadi kesalahan."}"
                    )
                }
            }
        }
    }

    fun signOut(onSignOut: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.signOut()
                _uiState.value = AuthUiState()
                onSignOut()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit) {
        if (email.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Masukkan email Anda.") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                authRepository.resetPassword(email.trim())
                _uiState.update { it.copy(isLoading = false, successMessage = "Link pemulihan password telah dikirim ke email Anda.") }
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal mengirim link: ${e.localizedMessage}") }
            }
        }
    }
}
