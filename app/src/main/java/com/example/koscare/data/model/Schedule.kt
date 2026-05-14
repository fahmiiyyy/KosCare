package com.example.koscare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Schedule(

    val id: String? = null,

    val user_id: String? = null,

    val title: String,

    val description: String,

    val schedule_date: String,

    val schedule_time: String,

    val status: Boolean = false,

    val created_at: String? = null
)