package com.example.koscare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koscare.data.model.Expense
import com.example.koscare.data.repository.AuthRepository
import com.example.koscare.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val repository =
        ExpenseRepository()

    private val authRepository =
        AuthRepository()

    private val _expenses =
        MutableStateFlow<List<Expense>>(emptyList())

    val expenses =
        _expenses.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading =
        _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage =
        _errorMessage.asStateFlow()

    fun getExpenses() {

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

                _expenses.value =
                    repository.getExpenses(userId)

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal memuat pengeluaran"

            } finally {

                _isLoading.value = false
            }
        }
    }

    private fun refreshExpenses() {

        viewModelScope.launch {

            try {

                val userId =
                    authRepository
                        .getCurrentUserId()

                if (userId != null) {

                    _expenses.value =
                        repository.getExpenses(userId)
                }

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal refresh data"
            }
        }
    }

    fun addExpense(
        title: String,
        amount: String
    ) {

        val parsedAmount =
            amount.trim().toIntOrNull()

        if (
            parsedAmount == null ||
            parsedAmount <= 0
        ) {

            _errorMessage.value =
                "Nominal tidak valid"

            return
        }

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

                val cal =
                    java.util.Calendar
                        .getInstance()

                val today =
                    String.format(

                        java.util.Locale
                            .getDefault(),

                        "%04d-%02d-%02d",

                        cal.get(
                            java.util.Calendar.YEAR
                        ),

                        cal.get(
                            java.util.Calendar.MONTH
                        ) + 1,

                        cal.get(
                            java.util.Calendar.DAY_OF_MONTH
                        )
                    )

                val expense =
                    Expense(

                        user_id = userId,

                        title =
                            title.trim(),

                        amount =
                            parsedAmount,

                        expense_date =
                            today
                    )

                repository.addExpense(expense)

                refreshExpenses()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menambah pengeluaran"
            }
        }
    }

    fun deleteExpense(
        expenseId: String
    ) {

        viewModelScope.launch {

            try {

                repository.deleteExpense(
                    expenseId
                )

                refreshExpenses()

            } catch (e: Exception) {

                e.printStackTrace()

                _errorMessage.value =
                    "Gagal menghapus pengeluaran"
            }
        }
    }

    fun clearError() {

        _errorMessage.value = null
    }
}