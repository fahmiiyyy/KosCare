package com.example.koscare.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://kogduirjtkpobbnelgrm.supabase.co/"

    private const val API_KEY =
        "sb_publishable_PbhCmhssp1aVEJxyVCc4tw_1LSg4puK"

    private val authInterceptor =
        Interceptor { chain ->

            val request =
                chain.request()
                    .newBuilder()
                    .addHeader(
                        "apikey",
                        API_KEY
                    )
                    .addHeader(
                        "Authorization",
                        "Bearer $API_KEY"
                    )
                    .build()

            chain.proceed(request)
        }

    private val logging =
        HttpLoggingInterceptor().apply {

            level =
                HttpLoggingInterceptor.Level.BODY
        }

    private val client =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

    val retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
}