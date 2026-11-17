package com.example.evently.core.auth

import com.example.evently.core.network.SupabaseNetwork
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.github.jan.supabase.gotrue.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
data class UserProfile(
    @SerialName("id") val id: String,
    @SerialName("email") val email: String?,
    @SerialName("full_name") val fullName: String?,
    @SerialName("role") val role: String = "student"
)

class AuthRepository {
    private val client = SupabaseNetwork.client
    private val profilesClient = client.from("profiles")

    suspend fun signUp(
        email: String,
        pass: String,
        fullName: String,
        role: String
    ): UserProfile {

        val user = client.auth.signUpWith(Email) {
            this.email = email
            this.password = pass

            data = buildJsonObject {
                put("full_name", fullName)
                put("role", role)
            }
        }

        val userId = user?.id
            ?: throw IllegalStateException("Registrasi gagal: User ID tidak ditemukan.")

        val profile = UserProfile(
            id = userId,
            email = email,
            fullName = fullName,
            role = role
        )

        try {
            profilesClient.insert(profile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return profile
    }

    suspend fun signIn(email: String, pass: String): UserProfile {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = pass
        }
        return getCurrentProfile() ?: throw IllegalStateException("Profil pengguna tidak ditemukan.")
    }

    suspend fun signOut() {
        client.auth.signOut()
    }

    suspend fun getCurrentProfile(): UserProfile? {
        val user = client.auth.currentUserOrNull() ?: return null
        try {
            val profiles = profilesClient.select {
                filter {
                    eq("id", user.id)
                }
            }.decodeList<UserProfile>()
            return profiles.firstOrNull() ?: UserProfile(
                id = user.id,
                email = user.email,
                fullName = user.userMetadata?.get("full_name")?.toString(),
                role = "student"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return UserProfile(id = user.id, email = user.email, fullName = null, role = "student")
        }
    }

    suspend fun resetPassword(email: String) {
        client.auth.resetPasswordForEmail(email)
    }

    suspend fun updatePassword(newPassword: String) {
        client.auth.modifyUser {
            password = newPassword
        }
    }

    fun isUserLoggedIn(): Boolean {
        return client.auth.currentUserOrNull() != null
    }
}
