package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val expenses by expenseViewModel
        .expenses
        .collectAsState()

    val isLoading by expenseViewModel
        .isLoading
        .collectAsState()

    LaunchedEffect(Unit) {

        expenseViewModel.getExpenses()
    }

    val totalExpense = expenses.sumOf {
        it.amount
    }

    LazyColumn(

        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(18.dp),

        verticalArrangement =
            Arrangement.spacedBy(16.dp)
    ) {

        item {

            Row(

                verticalAlignment =
                    Alignment.CenterVertically
            ) {


                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Text(

                    text = "Expenses KosCare",

                    color = Emerald,

                    fontSize = 32.sp,

                    fontWeight =
                        FontWeight.ExtraBold
                )
            }
        }

        item {

            Card(

                modifier =
                    Modifier.fillMaxWidth(),

                shape =
                    RoundedCornerShape(30.dp),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    )
            ) {

                Box(

                    modifier = Modifier
                        .background(

                            Brush.horizontalGradient(

                                colors = listOf(

                                    Emerald,
                                    Color(0xFF0F9D72)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {

                    Row(

                        modifier =
                            Modifier.fillMaxWidth(),

                        horizontalArrangement =
                            Arrangement.SpaceBetween,

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column {

                            Text(

                                text =
                                    "Total Expenses",

                                color =
                                    Color.White.copy(
                                        alpha = 0.8f
                                    )
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            Text(

                                text =
                                    formatRupiah(totalExpense),

                                fontSize = 34.sp,

                                color =
                                    Color.White,

                                fontWeight =
                                    FontWeight.ExtraBold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )

                            Text(

                                text =
                                    "This month",

                                color =
                                    Color.White.copy(
                                        alpha = 0.7f
                                    )
                            )
                        }

                        Icon(

                            imageVector =
                                Icons.Default.AccountBalanceWallet,

                            contentDescription =
                                null,

                            tint =
                                Color.White.copy(
                                    alpha = 0.25f
                                ),

                            modifier =
                                Modifier.size(90.dp)
                        )
                    }
                }
            }
        }

        item {

            Card(

                shape =
                    RoundedCornerShape(28.dp),

                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            Color.White
                    ),

                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
            ) {

                Column(

                    modifier =
                        Modifier.padding(20.dp)
                ) {

                    Text(

                        text =
                            "Quick Add",

                        fontSize = 24.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    OutlinedTextField(

                        value = title,

                        onValueChange = {
                            title = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Description")
                        },

                        shape =
                            RoundedCornerShape(16.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(14.dp)
                    )

                    OutlinedTextField(

                        value = amount,

                        onValueChange = {
                            amount = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {
                            Text("Amount")
                        },

                        shape =
                            RoundedCornerShape(16.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(22.dp)
                    )

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

                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp),

                        shape =
                            RoundedCornerShape(16.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Emerald
                            )
                    ) {

                        Text(

                            text =
                                "+ Add Expense",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {

            Text(

                text =
                    "Recent Expenses",

                fontSize = 24.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }

        if (isLoading) {

            item {

                CircularProgressIndicator()
            }

        } else {

            if (expenses.isEmpty()) {

                item {

                    Column {

                        Text(

                            text =
                                "Belum ada pengeluaran",

                            style =
                                MaterialTheme
                                    .typography
                                    .titleMedium
                        )

                        Text(
                            text =
                                "Tambahkan pengeluaran pertama anda!"
                        )
                    }
                }

            } else {

                items(expenses) { expense ->

                    Card(

                        shape =
                            RoundedCornerShape(24.dp),

                        colors =
                            CardDefaults.cardColors(
                                containerColor =
                                    Color.White
                            ),

                        elevation =
                            CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                    ) {

                        Row(

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),

                            horizontalArrangement =
                                Arrangement.SpaceBetween,

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Column {

                                Text(

                                    text =
                                        expense.title,

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleMedium,

                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(4.dp)
                                )

                                Text(
                                    text =
                                        expense.expense_date
                                )
                            }

                            Row(

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(

                                    text =
                                        formatRupiah(
                                            expense.amount
                                        ),

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        Emerald
                                )

                                IconButton(

                                    onClick = {

                                        expense.id?.let {

                                            expenseViewModel
                                                .deleteExpense(it)
                                        }
                                    }
                                ) {

                                    Icon(

                                        imageVector =
                                            Icons.Default.Delete,

                                        contentDescription =
                                            "Delete"
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

fun formatRupiah(
    amount: Int
): String {

    return NumberFormat
        .getCurrencyInstance(
            Locale("id", "ID")
        )
        .format(amount)
}