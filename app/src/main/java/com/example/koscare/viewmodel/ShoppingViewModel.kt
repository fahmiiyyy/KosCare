package com.example.koscare.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.ShoppingItem
import com.example.koscare.data.remote.SupabaseClientProvider
import com.example.koscare.data.repository.ShoppingRepository
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShoppingViewModel : ViewModel() {

    private val repository = ShoppingRepository()

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun getItems() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _items.value = repository.getItems()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat daftar belanja. Periksa koneksi internet kamu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addItem(itemName: String, quantity: Int, imageUrl: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = SupabaseClientProvider.client.auth.currentUserOrNull()
                if (user == null) {
                    _errorMessage.value = "Sesi kamu sudah berakhir. Silakan login ulang."
                    return@launch
                }
                repository.addItem(
                    ShoppingItem(
                        user_id = user.id,
                        item_name = itemName.trim(),
                        quantity = quantity,
                        image_url = imageUrl
                    )
                )
                getItems()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menambah item. Coba lagi ya."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            try {
                repository.deleteItem(itemId)
                getItems()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menghapus item. Coba lagi."
            }
        }
    }

    fun updateBoughtStatus(item: ShoppingItem) {
        viewModelScope.launch {
            try {
                repository.updateBoughtStatus(item)
                getItems()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memperbarui status item. Coba lagi."
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
            _errorMessage.value = null
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes()
                if (bytes == null) {
                    _errorMessage.value = "Gagal membaca gambar. Coba pilih foto lain."
                    return@launch
                }
                val fileName = "${System.currentTimeMillis()}.jpg"
                val imageUrl = repository.uploadImage(fileName, bytes)
                addItem(itemName = itemName, quantity = quantity, imageUrl = imageUrl)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengupload foto. Periksa koneksi internet kamu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}