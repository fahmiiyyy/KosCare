package com.example.koscare.data.remote

import com.example.koscare.data.model.Expense
import retrofit2.Response
import retrofit2.http.*

interface ExpenseApiService {

    @GET("rest/v1/expenses")
    suspend fun getExpenses(

        @Query("user_id")
        userId: String,

        @Header("Prefer")
        prefer: String = "return=representation"
    ): Response<List<Expense>>

    @POST("rest/v1/expenses")
    suspend fun addExpense(

        @Body expense: Expense,

        @Header("Prefer")
        prefer: String = "return=representation"
    ): Response<List<Expense>>

    @DELETE("rest/v1/expenses")
    suspend fun deleteExpense(

        @Query("id")
        id: String
    ): Response<Unit>
}