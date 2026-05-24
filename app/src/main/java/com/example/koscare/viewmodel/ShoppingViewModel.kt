package com.example.koscare.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.ShoppingItem
import com.example.koscare.data.repository.AuthRepository
import com.example.koscare.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel : ViewModel() {

    private val repository =
        ShoppingRepository()

    private val authRepository =
        AuthRepository()

    private val _items =
        MutableStateFlow<List<ShoppingItem>>(emptyList())

    val items: StateFlow<List<ShoppingItem>>
            = _items

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean>
            = _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?>
            = _errorMessage.asStateFlow()

    fun getItems() {

        viewModelScope.launch {

            _isLoading.value = true

            _errorMessage.value = null

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId == null) {

                    _errorMessage.value =
                        "User belum login"

                    return@launch
                }

                _items.value =
                    repository.getItems(userId)

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal memuat daftar belanja"

            } finally {

                _isLoading.value = false
            }
        }
    }

    private fun refreshItems() {

        viewModelScope.launch {

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId != null) {

                    _items.value =
                        repository.getItems(userId)
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal refresh data"
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

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId == null) {

                    _errorMessage.value =
                        "User belum login"

                    return@launch
                }

                val item =
                    ShoppingItem(

                        user_id = userId,

                        item_name =
                            itemName.trim(),

                        quantity = quantity,

                        image_url = imageUrl
                    )

                repository.addItem(item)

                refreshItems()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menambah item"
            }
        }
    }

    fun deleteItem(
        itemId: String
    ) {

        viewModelScope.launch {

            try {

                repository.deleteItem(itemId)

                refreshItems()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menghapus item"
            }
        }
    }

    fun updateBoughtStatus(
        item: ShoppingItem
    ) {

        viewModelScope.launch {

            try {

                repository.updateBoughtStatus(item)

                refreshItems()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal update status"
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

            _isLoading.value = true

            try {

                val inputStream =
                    context.contentResolver
                        .openInputStream(imageUri)

                val bytes =
                    inputStream?.readBytes()

                if (bytes == null) {

                    _errorMessage.value =
                        "Gagal membaca gambar"

                    return@launch
                }

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

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal upload gambar"

            } finally {

                _isLoading.value = false
            }
        }
    }

    fun clearError() {

        _errorMessage.value = null
    }
}