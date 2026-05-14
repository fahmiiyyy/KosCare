package com.example.koscare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(

    val id: String? = null,

    val user_id: String,

    val full_name: String? = null,

    val profile_image_url: String? = null,

    val created_at: String? = null
)