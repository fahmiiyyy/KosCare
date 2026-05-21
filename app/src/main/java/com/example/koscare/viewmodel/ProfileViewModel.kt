package com.example.koscare.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.UserProfile
import com.example.koscare.data.remote.SupabaseClientProvider
import com.example.koscare.data.repository.ProfileRepository
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun getProfile() {
        viewModelScope.launch {
            try {
                val user = SupabaseClientProvider.client.auth.currentUserOrNull()
                if (user != null) {
                    _profile.value = repository.getProfile(user.id)
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat profil"
            }
        }
    }

    fun updateProfile(fullName: String, imageUrl: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            _successMessage.value = null
            _errorMessage.value = null
            try {
                val user = SupabaseClientProvider.client.auth.currentUserOrNull()
                if (user != null) {
                    val profile = UserProfile(
                        user_id = user.id,
                        full_name = fullName,
                        profile_image_url = imageUrl
                    )
                    repository.updateProfile(profile)
                    _profile.value = repository.getProfile(user.id)
                    _successMessage.value = "Profil berhasil disimpan!"
                } else {
                    _errorMessage.value = "Sesi kamu sudah berakhir. Silakan login ulang."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menyimpan profil. Coba lagi ya."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadImageAndSaveProfile(context: Context, imageUri: Uri, fullName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _successMessage.value = null
            _errorMessage.value = null
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes()
                if (bytes != null) {
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val imageUrl = repository.uploadProfileImage(fileName, bytes)
                    val user = SupabaseClientProvider.client.auth.currentUserOrNull()
                    if (user != null) {
                        val profile = UserProfile(
                            user_id = user.id,
                            full_name = fullName,
                            profile_image_url = imageUrl
                        )
                        repository.updateProfile(profile)
                        _profile.value = repository.getProfile(user.id)
                        _successMessage.value = "Profil berhasil disimpan!"
                    }
                } else {
                    _errorMessage.value = "Gagal membaca gambar."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mengupload foto. Periksa koneksi internet kamu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            try {
                SupabaseClientProvider.client.auth.signOut()
                onLogout()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal logout."
            }
        }
    }

    fun clearMessages() {
        _successMessage.value = null
        _errorMessage.value = null
    }
}