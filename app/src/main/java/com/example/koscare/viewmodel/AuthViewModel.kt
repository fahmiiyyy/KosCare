package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess = _isSuccess.asStateFlow()

    fun register(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.register(email, password)
            _isLoading.value = false
            result.onSuccess {
                _isSuccess.value = true
            }
            result.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(email, password)
            _isLoading.value = false
            result.onSuccess {
                _isSuccess.value = true
            }
            result.onFailure {
                _errorMessage.value = it.message
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _isSuccess.value = false
        }
    }

    fun isUserLoggedIn(): Boolean {
        return repository.isUserLoggedIn()
    }
}