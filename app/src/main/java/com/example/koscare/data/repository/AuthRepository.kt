package com.example.koscare.data.repository

import com.example.koscare.data.remote.SupabaseClientProvider
import io.github.jan.supabase.gotrue.auth

class AuthRepository {

    private val client = SupabaseClientProvider.client

    suspend fun register(
        email: String,
        password: String
    ): Result<Unit> {

        return try {
            client.auth.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {

        return try {

            client.auth.signInWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        client.auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return client.auth.currentSessionOrNull() != null
    }

    fun getCurrentUserId(): String? {
        return client.auth.currentUserOrNull()?.id
    }
}