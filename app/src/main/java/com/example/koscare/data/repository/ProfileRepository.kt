package com.example.koscare.data.repository

import com.example.koscare.data.model.UserProfile
import com.example.koscare.data.remote.ProfileApiService
import com.example.koscare.data.remote.RetrofitClient
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.storage.storage

class ProfileRepository {

    private val apiService =
        RetrofitClient
            .retrofit
            .create(
                ProfileApiService::class.java
            )

    private val client =
        SupabaseClientProvider.client

    suspend fun getProfile(
        userId: String
    ): UserProfile? {

        val response =
            apiService.getProfile(
                userId = "eq.$userId"
            )

        return response
            .body()
            ?.firstOrNull()
    }

    suspend fun updateProfile(
        profile: UserProfile
    ) {

        val existingProfile =
            getProfile(profile.user_id)

        if (existingProfile == null) {

            apiService.insertProfile(profile)

        } else {

            apiService.updateProfile(

                userId =
                    "eq.${profile.user_id}",

                body = mapOf(

                    "full_name"
                            to profile.full_name,

                    "profile_image_url"
                            to profile.profile_image_url
                )
            )
        }
    }

    suspend fun uploadProfileImage(
        fileName: String,
        bytes: ByteArray
    ): String {

        client
            .storage
            .from("profile-images")
            .upload(
                path = fileName,
                data = bytes
            )

        return client
            .storage
            .from("profile-images")
            .publicUrl(fileName)
    }
}