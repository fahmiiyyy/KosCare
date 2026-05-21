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

    private val repository = ExpenseRepository()
    private val authRepository = AuthRepository()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses = _expenses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun getExpenses() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                _expenses.value = repository.getExpenses()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat pengeluaran. Periksa koneksi internet kamu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun refreshExpenses() {
        viewModelScope.launch {
            try {
                _expenses.value = repository.getExpenses()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memperbarui pengeluaran. Coba lagi."
            }
        }
    }

    fun addExpense(title: String, amount: String) {
        val parsedAmount = amount.trim().toIntOrNull()
        if (parsedAmount == null || parsedAmount <= 0) {
            _errorMessage.value = "Nominal harus berupa angka dan lebih dari 0."
            return
        }
        viewModelScope.launch {
            _errorMessage.value = null
            try {
                val userId = authRepository.getCurrentUserId()
                if (userId == null) {
                    _errorMessage.value = "Sesi kamu sudah berakhir. Silakan login ulang."
                    return@launch
                }
                val today = run {
                    val cal = java.util.Calendar.getInstance()
                    String.format(
                        java.util.Locale.getDefault(),
                        "%04d-%02d-%02d",
                        cal.get(java.util.Calendar.YEAR),
                        cal.get(java.util.Calendar.MONTH) + 1,
                        cal.get(java.util.Calendar.DAY_OF_MONTH)
                    )
                }
                val expense = Expense(
                    user_id = userId,
                    title = title.trim(),
                    amount = parsedAmount,
                    expense_date = today
                )

                _expenses.value = _expenses.value + expense

                repository.addExpense(expense)
                refreshExpenses()
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menambah pengeluaran."
                refreshExpenses()
            }
        }
    }

    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                _expenses.value = _expenses.value.filter { it.id != expenseId }
                repository.deleteExpense(expenseId)
            } catch (e: Exception) {
                _errorMessage.value = "Gagal menghapus pengeluaran."
                refreshExpenses()
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}