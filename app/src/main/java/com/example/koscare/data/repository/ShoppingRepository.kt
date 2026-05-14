package com.example.koscare.data.repository

import com.example.koscare.data.model.ShoppingItem
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload

class ShoppingRepository {

    private val client = SupabaseClientProvider.client

    suspend fun getItems(): List<ShoppingItem> {

        return client
            .from("shopping_items")
            .select()
            .decodeList<ShoppingItem>()
    }

    suspend fun addItem(
        item: ShoppingItem
    ) {

        client
            .from("shopping_items")
            .insert(item)
    }

    suspend fun deleteItem(
        itemId: String
    ) {

        client
            .from("shopping_items")
            .delete {

                filter {

                    eq("id", itemId)
                }
            }
    }

    suspend fun updateBoughtStatus(
        item: ShoppingItem
    ) {

        client
            .from("shopping_items")
            .update(
                {
                    set("is_bought", !item.is_bought)
                }
            ) {

                filter {

                    eq("id", item.id!!)
                }
            }
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