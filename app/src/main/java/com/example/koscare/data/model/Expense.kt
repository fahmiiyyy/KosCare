package com.example.koscare.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Expense(

    val id: String? = null,

    val user_id: String,

    val title: String,

    val amount: Int,

    val expense_date: String
)