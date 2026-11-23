package com.example.evently.core.auth

import com.example.evently.core.network.SupabaseNetwork
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class UserProfile(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("role") val role: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null
)

class AuthRepository {
    private val client = SupabaseNetwork.client
    private val profilesClient = client.from("profiles")

    /** The existing auth.users trigger provisions the profile from this metadata. */
    suspend fun signUp(email: String, pass: String, fullName: String, role: String) {
        val databaseRole = role.toDatabaseRole()
        client.auth.signUpWith(Email) {
            this.email = email
            this.password = pass
            data = buildJsonObject {
                put("full_name", fullName)
                put("role", databaseRole)
            }
        }
    }

    suspend fun signIn(email: String, pass: String): UserProfile {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = pass
        }
        return getCurrentProfile()
            ?: throw IllegalStateException("Profil akun tidak ditemukan. Hubungi administrator.")
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    suspend fun getCurrentProfile(): UserProfile? {
        val user = client.auth.currentUserOrNull() ?: return null
        val profile = profilesClient.select {
            filter { eq("id", user.id) }
        }.decodeList<UserProfile>().firstOrNull()
            ?: throw IllegalStateException("Profil akun tidak ditemukan. Hubungi administrator.")
        return profile.copy(
            email = profile.email ?: user.email,
            fullName = profile.fullName ?: user.userMetadata?.get("full_name")?.toString(),
            role = profile.role.toDomainRole()
        )
    }

    suspend fun resetPassword(email: String) {
        client.auth.resetPasswordForEmail(email)
    }

    suspend fun updatePassword(newPassword: String) {
        client.auth.modifyUser { password = newPassword }
    }

    fun isUserLoggedIn(): Boolean = client.auth.currentUserOrNull() != null

    private fun String.toDatabaseRole(): String = when (lowercase()) {
        "student", "mahasiswa" -> "mahasiswa"
        "organizer", "panitia" -> "panitia"
        "admin" -> "admin"
        else -> throw IllegalArgumentException("Pilihan role tidak valid.")
    }

    private fun String.toDomainRole(): String = when (lowercase()) {
        "mahasiswa", "student" -> "student"
        "panitia", "organizer" -> "organizer"
        "admin" -> "admin"
        else -> throw IllegalStateException("Role akun tidak valid. Hubungi administrator.")
    }
}
