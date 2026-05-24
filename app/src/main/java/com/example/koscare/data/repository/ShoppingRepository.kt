package com.example.koscare.data.repository

import com.example.koscare.data.model.ShoppingItem
import com.example.koscare.data.remote.RetrofitClient
import com.example.koscare.data.remote.ShoppingApiService
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.storage.storage

class ShoppingRepository {

    private val apiService =
        RetrofitClient
            .retrofit
            .create(
                ShoppingApiService::class.java
            )

    private val client =
        SupabaseClientProvider.client

    suspend fun getItems(
        userId: String
    ): List<ShoppingItem> {

        val response =
            apiService.getItems(
                userId = "eq.$userId"
            )

        return response.body() ?: emptyList()
    }

    suspend fun addItem(
        item: ShoppingItem
    ) {

        apiService.addItem(item)
    }

    suspend fun deleteItem(
        itemId: String
    ) {

        apiService.deleteItem(
            id = "eq.$itemId"
        )
    }

    suspend fun updateBoughtStatus(
        item: ShoppingItem
    ) {

        apiService.updateBoughtStatus(

            id = "eq.${item.id}",

            body = mapOf(
                "is_bought" to !item.is_bought
            )
        )
    }

    suspend fun uploadImage(
        fileName: String,
        bytes: ByteArray
    ): String {

        client
            .storage
            .from("shopping-images")
            .upload(
                path = fileName,
                data = bytes
            )

        return client
            .storage
            .from("shopping-images")
            .publicUrl(fileName)
    }
}