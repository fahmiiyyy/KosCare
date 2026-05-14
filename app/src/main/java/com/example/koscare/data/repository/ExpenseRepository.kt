package com.example.koscare.data.repository

import com.example.koscare.data.model.Expense
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from

class ExpenseRepository {

    private val client = SupabaseClientProvider.client

    suspend fun getExpenses(): List<Expense> {

        return client
            .from("expenses")
            .select()
            .decodeList<Expense>()
    }

    suspend fun addExpense(
        expense: Expense
    ) {

        client
            .from("expenses")
            .insert(expense)
    }

    suspend fun deleteExpense(
        expenseId: String
    ) {

        client
            .from("expenses")
            .delete {

                filter {

                    eq("id", expenseId)
                }
            }
    }
}