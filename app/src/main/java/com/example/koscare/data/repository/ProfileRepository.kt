package com.example.koscare.data.repository

import com.example.koscare.data.model.UserProfile
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload

class ProfileRepository {

    private val client = SupabaseClientProvider.client

    suspend fun getProfile(userId: String): UserProfile? {
        return try {
            client
                .from("profiles")
                .select {
                    filter { eq("user_id", userId) }
                }
                .decodeSingleOrNull<UserProfile>()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateProfile(profile: UserProfile) {
        val existing = getProfile(profile.user_id)

        if (existing == null) {
            // Belum ada profil → insert baru
            client
                .from("profiles")
                .insert(profile)
        } else {
            // Sudah ada → update berdasarkan user_id
            client
                .from("profiles")
                .update({
                    set("full_name", profile.full_name)
                    set("profile_image_url", profile.profile_image_url)
                }) {
                    filter { eq("user_id", profile.user_id) }
                }
        }
    }

    suspend fun uploadProfileImage(
        fileName: String,
        bytes: ByteArray
    ): String {
        client
            .storage
            .from("profile-images")
            .upload(path = fileName, data = bytes)

        return client
            .storage
            .from("profile-images")
            .publicUrl(fileName)
    }
}