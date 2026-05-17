package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.Expense
import com.example.koscare.data.model.UserProfile
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val client = SupabaseClientProvider.client

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadDashboard() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = client.auth.currentUserOrNull() ?: return@launch

                val profile = client
                    .from("profiles")
                    .select {
                        filter { eq("user_id", user.id) }
                    }
                    .decodeSingleOrNull<UserProfile>()

                val expenses = client
                    .from("expenses")
                    .select {
                        filter { eq("user_id", user.id) }
                    }
                    .decodeList<Expense>()

                val totalExpense = expenses.sumOf { it.amount.toDouble() }

                _uiState.value = HomeUiState(
                    totalExpense = totalExpense,
                    userName = profile?.full_name?.takeIf { it.isNotBlank() } ?: "Mahasiswa"
                )

            } catch (e: Exception) {
            } finally {
                _isLoading.value = false
            }
        }
    }
}