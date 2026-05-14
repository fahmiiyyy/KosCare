package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.ShoppingItem
import com.example.koscare.data.remote.SupabaseClientProvider
import com.example.koscare.data.repository.ShoppingRepository
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.net.Uri
import android.content.Context

class ShoppingViewModel : ViewModel() {

    private val repository = ShoppingRepository()

    private val _items =
        MutableStateFlow<List<ShoppingItem>>(emptyList())

    val items: StateFlow<List<ShoppingItem>>
            = _items

    fun getItems() {

        viewModelScope.launch {

            try {

                _items.value =
                    repository.getItems()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun addItem(
        itemName: String,
        quantity: Int,
        imageUrl: String?
    ) {

        viewModelScope.launch {

            try {

                val user =
                    SupabaseClientProvider
                        .client
                        .auth
                        .currentUserOrNull()

                if (user != null) {

                    val item = ShoppingItem(

                        user_id = user.id,

                        item_name = itemName,

                        quantity = quantity,

                        image_url = imageUrl
                    )

                    repository.addItem(item)

                    getItems()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun deleteItem(
        itemId: String
    ) {

        viewModelScope.launch {

            try {

                repository.deleteItem(itemId)

                getItems()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun updateBoughtStatus(
        item: ShoppingItem
    ) {

        viewModelScope.launch {

            try {

                repository.updateBoughtStatus(item)

                getItems()

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun uploadImageAndAddItem(
        context: Context,
        imageUri: Uri,
        itemName: String,
        quantity: Int
    ) {

        viewModelScope.launch {

            try {

                val inputStream =
                    context.contentResolver
                        .openInputStream(imageUri)

                val bytes =
                    inputStream?.readBytes()

                if (bytes != null) {

                    val fileName =
                        "${System.currentTimeMillis()}.jpg"

                    val imageUrl =
                        repository.uploadImage(
                            fileName,
                            bytes
                        )

                    addItem(
                        itemName = itemName,
                        quantity = quantity,
                        imageUrl = imageUrl
                    )
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}