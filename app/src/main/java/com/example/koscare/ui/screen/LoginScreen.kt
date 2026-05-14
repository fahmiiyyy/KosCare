package com.example.koscare.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.koscare.ui.theme.Background
import com.example.koscare.ui.theme.Emerald
import com.example.koscare.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var isLoginMode by remember {
        mutableStateOf(true)
    }

    val isLoading by authViewModel.isLoading.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    val isSuccess by authViewModel.isSuccess.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "KosCare",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Emerald
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isLoginMode) {
                "Login ke akun anda"
            } else {
                "Buat akun baru"
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Email")
            },

            singleLine = true,

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Password")
            },

            singleLine = true,

            visualTransformation = PasswordVisualTransformation(),

            shape = RoundedCornerShape(20.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {

                if (isLoginMode) {

                    authViewModel.login(
                        email = email,
                        password = password
                    )

                } else {

                    authViewModel.register(
                        email = email,
                        password = password
                    )
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Emerald
            ),

            shape = RoundedCornerShape(20.dp)
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary
                )

            } else {

                Text(
                    text = if (isLoginMode) {
                        "Login"
                    } else {
                        "Register"
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                isLoginMode = !isLoginMode
            }
        ) {

            Text(
                text = if (isLoginMode) {
                    "Belum punya akun? Register"
                } else {
                    "Sudah punya akun? Login"
                }
            )
        }

        errorMessage?.let {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (isSuccess) {

            onLoginSuccess()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isLoginMode) {
                    "Login berhasil"
                } else {
                    "Register berhasil"
                },

                color = Emerald
            )
        }
    }
}