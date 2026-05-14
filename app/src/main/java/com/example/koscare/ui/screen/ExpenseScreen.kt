package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ExpenseScreen(
    expenseViewModel: ExpenseViewModel = viewModel()
) {

    var title by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    val expenses by expenseViewModel.expenses.collectAsState()

    val isLoading by expenseViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        expenseViewModel.getExpenses()
    }

    val totalExpense = expenses.sumOf {
        it.amount
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {

        Text(
            text = "Expense Tracker",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),

            colors = CardDefaults.cardColors(
                containerColor = Emerald
            ),

            shape = RoundedCornerShape(24.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {

                Text(
                    text = "Total Pengeluaran",
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = formatRupiah(totalExpense),

                    style = MaterialTheme.typography.headlineMedium,

                    color = MaterialTheme.colorScheme.onPrimary,

                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Nama Pengeluaran")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = {
                amount = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Jumlah")
            },

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {

                if (
                    title.isNotEmpty() &&
                    amount.isNotEmpty()
                ) {

                    expenseViewModel.addExpense(
                        title = title,
                        amount = amount.toInt()
                    )

                    title = ""
                    amount = ""
                }
            },

            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Tambah Pengeluaran")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {

            CircularProgressIndicator()

        } else {

            if (expenses.isEmpty()) {

                Column {

                    Text(
                        text = "Belum ada pengeluaran",

                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Tambahkan pengeluaran pertama anda!"
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(expenses) { expense ->

                        Card(
                            shape = RoundedCornerShape(20.dp),

                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),

                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Column {

                                    Text(
                                        text = expense.title,

                                        style = MaterialTheme.typography.titleMedium,

                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = expense.expense_date
                                    )
                                }

                                Row {

                                    Text(
                                        text = formatRupiah(expense.amount),

                                        fontWeight = FontWeight.Bold,

                                        color = Emerald
                                    )

                                    IconButton(
                                        onClick = {

                                            expense.id?.let {

                                                expenseViewModel.deleteExpense(it)
                                            }
                                        }
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatRupiah(
    amount: Int
): String {

    return NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
}