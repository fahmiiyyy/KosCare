package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val client =
        SupabaseClientProvider.client

    private val _uiState =
        MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState>
            = _uiState

    fun loadDashboard() {

        viewModelScope.launch {

            try {

                val user =
                    client
                        .auth
                        .currentUserOrNull()

                if (user != null) {

                    val expenses =
                        client
                            .from("expenses")
                            .select {

                                filter {

                                    eq(
                                        "user_id",
                                        user.id
                                    )
                                }
                            }
                            .decodeList<Map<String, Any>>()

                    val profile =
                        client
                            .from("profiles")
                            .select {

                                filter {

                                    eq(
                                        "user_id",
                                        user.id
                                    )
                                }
                            }
                            .decodeSingleOrNull<Map<String, Any>>()

                    val totalExpense =
                        expenses.sumOf {

                            (
                                    it["amount"]
                                            as? Number
                                    )?.toDouble() ?: 0.0
                        }

                    _uiState.value =
                        HomeUiState(

                            totalExpense =
                                totalExpense,

                            userName =
                                profile?.get(
                                    "full_name"
                                ) as? String
                                    ?: "User"
                        )
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }
}