package com.example.koscare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ShoppingItem(

    val id: String? = null,

    val user_id: String,

    val item_name: String,

    val quantity: Int,

    val image_url: String? = null,

    val is_bought: Boolean = false
)