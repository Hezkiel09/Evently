package com.example.evently.core.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json

object SupabaseNetwork {
    val client = createSupabaseClient(
        supabaseUrl = "https://ajcycfqckypxbqmrbwjr.supabase.co",
        supabaseKey = "sb_publishable_AHaipZtzap1fC0gXDmMMNQ_jFDjSIyN"
    ) {
        install(Postgrest)

        defaultSerializer = KotlinXSerializer(Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        })
    }
}