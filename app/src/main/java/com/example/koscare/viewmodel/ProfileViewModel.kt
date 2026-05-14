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
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val repository =
        ProfileRepository()

    private val _profile =
        MutableStateFlow<UserProfile?>(null)

    val profile: StateFlow<UserProfile?>
            = _profile

    fun getProfile() {

        viewModelScope.launch {

            try {

                val user =
                    SupabaseClientProvider
                        .client
                        .auth
                        .currentUserOrNull()

                if (user != null) {

                    _profile.value =
                        repository.getProfile(user.id)
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun updateProfile(
        fullName: String,
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

                    val profile = UserProfile(

                        user_id = user.id,

                        full_name = fullName,

                        profile_image_url = imageUrl
                    )

                    repository.updateProfile(profile)

                    getProfile()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun uploadImageAndSaveProfile(
        context: Context,
        imageUri: Uri,
        fullName: String
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
                        repository.uploadProfileImage(
                            fileName,
                            bytes
                        )

                    updateProfile(
                        fullName = fullName,
                        imageUrl = imageUrl
                    )
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    fun logout(
        onLogout: () -> Unit
    ) {

        viewModelScope.launch {

            SupabaseClientProvider
                .client
                .auth
                .signOut()

            onLogout()
        }
    }
}