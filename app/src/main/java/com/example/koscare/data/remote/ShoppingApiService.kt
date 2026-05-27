package com.example.koscare.data.remote

import com.example.koscare.data.model.ShoppingItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingApiService {

    @GET("rest/v1/shopping_items")
    suspend fun getItems(

        @Query("select")
        select: String = "*",

        @Query("user_id")
        userId: String

    ): Response<List<ShoppingItem>>

    @POST("rest/v1/shopping_items")
    suspend fun addItem(

        @Body item: ShoppingItem,

        @Header("Prefer")
        prefer: String = "return=representation"

    ): Response<List<ShoppingItem>>

    @DELETE("rest/v1/shopping_items")
    suspend fun deleteItem(

        @Query("id")
        id: String

    ): Response<Unit>

    @PATCH("rest/v1/shopping_items")
    suspend fun updateBoughtStatus(

        @Query("id")
        id: String,

        @Body body: Map<String, Boolean>

    ): Response<Unit>
}