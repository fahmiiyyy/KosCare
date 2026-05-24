package com.example.koscare.data.repository

import com.example.koscare.data.model.Expense
import com.example.koscare.data.remote.ExpenseApiService
import com.example.koscare.data.remote.RetrofitClient

class ExpenseRepository {

    private val apiService =
        RetrofitClient
            .retrofit
            .create(
                ExpenseApiService::class.java
            )

    suspend fun getExpenses(
        userId: String
    ): List<Expense> {

        val response =
            apiService.getExpenses(
                userId = "eq.$userId"
            )

        return response.body() ?: emptyList()
    }

    suspend fun addExpense(
        expense: Expense
    ) {

        apiService.addExpense(expense)
    }

    suspend fun deleteExpense(
        expenseId: String
    ) {

        apiService.deleteExpense(
            id = "eq.$expenseId"
        )
    }
}