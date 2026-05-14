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

    fun getExpenses() {

        viewModelScope.launch {

            _isLoading.value = true

            _expenses.value = repository.getExpenses()

            _isLoading.value = false
        }
    }

    fun addExpense(
        title: String,
        amount: Int
    ) {

        viewModelScope.launch {

            val userId = authRepository.getCurrentUserId()

            if (userId == null) return@launch

            val expense = Expense(
                user_id = userId,
                title = title,
                amount = amount,
                expense_date = java.time.LocalDate.now().toString()
            )

            repository.addExpense(expense)

            getExpenses()
        }
    }

    fun deleteExpense(
        expenseId: String
    ) {

        viewModelScope.launch {

            repository.deleteExpense(expenseId)

            getExpenses()
        }
    }
}